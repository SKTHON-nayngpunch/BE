package com.skthon.nayngpunch.global.s3.service;

import com.skthon.nayngpunch.global.s3.entity.PathName;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface S3Service {

    String uploadFile(PathName pathName, MultipartFile file);

    String createKeyName(PathName pathName);

    void deleteFile(String keyName);

    List<String> getAllFiles(PathName pathName);

    String extractKeyNameFromUrl(String imageUrl);

    void fileExists(String keyName);
}
