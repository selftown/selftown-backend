package com.github.selftown.global.auth.OAuth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("${api_prefix}/oauth2/kakao")
public class OAuthController {
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    private final String TOKEN_REQUEST_URL = "https://kauth.kakao.com/oauth/token";
    private final String USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";


    @GetMapping
    public void getKakaoToken(@RequestParam String code) {
        System.out.println(code);
        RestTemplate restTemplate = new RestTemplate();

        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "authorization_code");
        params.put("client_id", clientId);
        params.put("redirect_uri", redirectUri);
        params.put("code", code);
        params.put("client_secret", clientSecret);

        ResponseEntity<Map> response = restTemplate.postForEntity(TOKEN_REQUEST_URL, params, Map.class);
        Map<String, Object> responseBody = response.getBody();

        String accessToken = (String) responseBody.get("access_token");


        System.out.println(accessToken);
    }
}
