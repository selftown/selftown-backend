package com.github.selftown.global.auth.OAuth.service;

import com.github.selftown.domain.user.domain.User;
import com.github.selftown.domain.user.repository.UserRepository;
import com.github.selftown.global.auth.OAuth.dto.KakaoTokenResponse;
import com.github.selftown.global.auth.OAuth.dto.KakaoUserResponse;
import com.github.selftown.global.auth.OAuth.dto.LoginResponse;
import com.github.selftown.global.auth.OAuth.dto.UserInfo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuthService {

    private final UserRepository userRepository;
    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    @Value("${kakao.client.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.token-url}")
    private String tokenRequestUrl;

    @Value("${kakao.user-info-url}")
    private String userInfoUrl;

    private WebClient webClient;
    private WebClient userClient;
    @PostConstruct
    private void init() {
        this.webClient = WebClient.builder().baseUrl(tokenRequestUrl.trim()).build();
        this.userClient = WebClient.builder().baseUrl(userInfoUrl.trim()).build();
    }
    public KakaoTokenResponse getKakaoToken(String code) {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        params.add("client_secret", clientSecret);


        return webClient.post()
                .uri(tokenRequestUrl)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData(params))
                .retrieve()
                .bodyToMono(KakaoTokenResponse.class)
                .block(); // 블록킹 방식으로 반환 (단순하게 사용 가능)
    }

    public KakaoUserResponse getUserInfo(String accessToken) {
        return userClient.get()
                .uri(uriBuilder -> uriBuilder.build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Map.class)
                .map(responseBody -> {
                    Map<String, Object> kakaoAccount = (Map<String, Object>) responseBody.get("kakao_account");
                    String nickname = null;
                    String profileImage = null;
                    String email = null;

                    if (kakaoAccount != null) {
                        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                        if (profile != null) {
                            nickname = (String) profile.get("nickname");
                            profileImage = (String) profile.get("profile_image_url");
                        }
                        email = (String) kakaoAccount.get("email");
                    }

                    ;
                    return KakaoUserResponse.builder()
                            .id((Long) responseBody.get("id"))
                            .nickname(nickname)
                            .profileImage(profileImage)
                            .email(email)
                            .build();
                })
                .block(); // 블록킹 방식으로 반환
    }

    public LoginResponse kakaoLogin(String code) {
        // 1. 카카오 토큰 요청
        KakaoTokenResponse kakaoTokenResponse = getKakaoToken(code);
        String accessToken = kakaoTokenResponse.getAccessToken();


        KakaoUserResponse kakaoUserResponse = getUserInfo(accessToken);

        Long id = kakaoUserResponse.getId();
        System.out.println(kakaoUserResponse.toString());
        // 3. 유저가 이미 존재하는지 체크
        User user = userRepository.findByKakaoId(id)
                .orElseGet(() -> {
                    // 회원가입이 안 돼 있는 경우 회원가입을 진행합니다.
                    User newUser = User.builder()
                            .kakaoId(id)
                            .userName(kakaoUserResponse.getNickname())
                            .nickname(kakaoUserResponse.getNickname())
                            .email(kakaoUserResponse.getEmail())
                            .profileImage(kakaoUserResponse.getProfileImage())
                            .role(User.Role.MEMBER)
                            .build();
                    return userRepository.save(newUser);
                });

        String token = generateFakeToken(user);

        return LoginResponse.builder()
                .token(token)
                .userInfo(UserInfo.builder()
                        .id(user.getKakaoId())
                        .nickname(user.getNickname())
                        .profileImage(user.getProfileImage())
                        .email(user.getEmail())
                        .build())
                .build();
    }

    private String generateFakeToken(User user) {
        // 토큰이 아직 준비되지 않았으므로 임시로 유저 정보를 기반으로 한 고정 토큰을 생성합니다.
        return "dummy-token-for-user-" + user.getKakaoId();
    }
}
