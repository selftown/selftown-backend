package com.github.selftown.global.config;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
@Service
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private Auth auth;
    private final OAuth2 oauth2 = new OAuth2();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Auth {
        @Value("${TOKEN_SECRET}")
        private String tokenSecret;

        @Value("30")
        private Long accessTokenExpiry;

        @Value("10080")
        private long refreshTokenExpiry;

        @Value("localhost")
        private String cookieDomain;
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    public static final class OAuth2 {
        @Value("${app.oauth2.client-redirect-uri}")
        private List<String> clientRedirectUri;

    }
}
