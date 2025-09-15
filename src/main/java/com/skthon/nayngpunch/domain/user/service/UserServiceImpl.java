package com.skthon.nayngpunch.domain.user.service;

import com.skthon.nayngpunch.domain.user.dto.request.SignUpRequest;
import com.skthon.nayngpunch.domain.user.dto.response.SignUpResponse;
import com.skthon.nayngpunch.domain.user.dto.response.UserDetailResponse;
import com.skthon.nayngpunch.domain.user.dto.response.UserResponse;
import com.skthon.nayngpunch.domain.user.entity.User;
import com.skthon.nayngpunch.domain.user.exception.UserErrorCode;
import com.skthon.nayngpunch.domain.user.mapper.UserMapper;
import com.skthon.nayngpunch.domain.user.repository.UserRepository;
import com.skthon.nayngpunch.global.exception.CustomException;
import com.skthon.nayngpunch.global.s3.entity.PathName;
import com.skthon.nayngpunch.global.s3.exception.S3ErrorCode;
import com.skthon.nayngpunch.global.s3.service.S3Service;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final S3Service s3Service;

    @Override
    @Transactional
    public SignUpResponse signUp(SignUpRequest request, MultipartFile image) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new CustomException(UserErrorCode.USERNAME_ALREADY_EXISTS);
        }

        // 비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        String imageUrl;

        try {
            imageUrl = s3Service.uploadFile(PathName.PROFILE_IMAGE, image);
        } catch (Exception e) {
            throw new CustomException(S3ErrorCode.FILE_SERVER_ERROR);
        }

        User user = userMapper.toUser(request, encodedPassword, imageUrl);
        User savedUser = userRepository.save(user);

        log.info("새로운 사용자 생성: {}", savedUser.getUsername());

        return userMapper.toSignUpResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("인증 실패 - 인증 정보 없음");
            throw new CustomException(UserErrorCode.UNAUTHORIZED);
        }

        Object principal = authentication.getPrincipal();
        String username = "";

        try {
            switch (principal) {
                case OAuth2User oauthUser -> {
                    Object email = oauthUser.getAttribute("email");
                    if (email != null) {
                        username = (String) email;
                    } else {
                        Map<String, Object> kakaoAccount = oauthUser.getAttribute("kakao_account");
                        if (kakaoAccount != null && kakaoAccount.containsKey("email")) {
                            username = (String) kakaoAccount.get("email");
                        }
                    }
                }
                case String str -> username = str;
                case UserDetails userDetails -> username = userDetails.getUsername();
                default -> {
                    log.error("인증 실패 - Principal 타입 알 수 없음: {}", principal.getClass());
                    throw new CustomException(UserErrorCode.UNAUTHORIZED);
                }
            }
        } catch (Exception e) {
            log.error("인증 정보 추출 중 오류", e);
            throw new CustomException(UserErrorCode.UNAUTHORIZED);
        }

        if (username == null || username.isBlank()) {
            log.error("인증 실패 - 추출된 username이 null 또는 빈 문자열");
            throw new CustomException(UserErrorCode.UNAUTHORIZED);
        }

        log.debug("JWT에서 추출한 email: {}", username);

        return userRepository
            .findByUsername(username)
            .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUser() {

        User currentUser = getCurrentUser();

        return userMapper.toUserResponse(currentUser);
    }

    @Override
    @Transactional
    public String updateProfileImage(MultipartFile profileImage) {

        User user = getCurrentUser();

        if (profileImage == null || profileImage.isEmpty()) {
            log.warn("프로필 이미지 변경 요청이 비어있음 - userId: {}", user.getId());
            return user.getProfileImageUrl();
        }

        String oldImageUrl = user.getProfileImageUrl();
        String newImageUrl;

        try {
            newImageUrl = s3Service.uploadFile(PathName.PROFILE_IMAGE, profileImage);

            if (oldImageUrl != null
                && !oldImageUrl.isEmpty()
                && !oldImageUrl.startsWith("http://k.kakaocdn.net")) {
                s3Service.deleteFile(oldImageUrl);
            }
        } catch (Exception e) {
            log.error("S3 파일 업로드 실패(교체) - userId: {}", user.getId(), e);
            throw new CustomException(S3ErrorCode.FILE_SERVER_ERROR);
        }

        user.updateProfileImageUrl(newImageUrl);
        log.info("사용자 프로필 이미지 변경 - userId: {}, newImageUrl: {}", user.getId(), newImageUrl);

        return newImageUrl;
    }

    @Override
    public UserResponse getUserProfile(Long userId) {
        User user =
            userRepository
                .findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        log.info("사용자 프로필 조회 성공 - userId: {}", userId);
        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDetailResponse> getAllUsers() {

        List<User> users = userRepository.findAll();

        return users.stream().map(userMapper::toUserDetailResponse).toList();
    }

}
