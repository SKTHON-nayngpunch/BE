package com.skthon.nayngpunch.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Value("${swagger.server.profile}")
    private String profileUrl;

    @Value("${swagger.server.name}")
    private String profileName;

    @Bean
    public OpenAPI customOpenAPI() {
        Server server = new Server().url(profileUrl + contextPath).description(profileName + "Server");

        return new OpenAPI()
            .addServersItem(server)
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
            .components(
                new Components()
                    .addSecuritySchemes(
                        "bearerAuth",
                        new SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")))
            .info(
                new Info()
                    .title("TEAM : NayngNayng-Punch API 명세서")
                    .version("1.0")
                    .description(
                        """
                    # 서경대학교 교내해커톤 프로젝트 - 냥냥펀치

                    ## 주의사항
                    - 파일 업로드 크기 제한: 5MB (1개 파일 크기)
                    """));
    }

    @Bean
    public GroupedOpenApi apiGroup() {
        return GroupedOpenApi.builder().group("api").pathsToMatch("/**").build();
    }
}
