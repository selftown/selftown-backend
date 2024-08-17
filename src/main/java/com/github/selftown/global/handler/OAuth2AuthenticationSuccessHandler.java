package com.github.selftown.global.handler;

import com.github.selftown.global.auth.PrincipalDetails;
import com.github.selftown.global.jwt.AuthToken;
import com.github.selftown.global.jwt.AuthTokenProvider;
import com.github.selftown.global.config.AppProperties;
import com.github.selftown.global.util.CookieUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final AuthTokenProvider authTokenProvider;
    private final AppProperties appProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 인증 성공 시 JWT 토큰 생성
        log.info("Authentication successful for user: {}", authentication.getName());

        // PrincipalDetails로부터 사용자 정보 가져오기
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Long userId = principalDetails.getId();
        String role = principalDetails.getRole().getCode();

        // JWT 토큰 생성
        Date now = new Date();
        Date accessTokenExpiry = new Date(now.getTime() + appProperties.getAuth().getAccessTokenExpiry() * 60 * 1000); // 액세스 토큰 만료 시간
        Date refreshTokenExpiry = new Date(now.getTime() + appProperties.getAuth().getRefreshTokenExpiry() * 60 * 1000); // 리프레시 토큰 만료 시간

        AuthToken accessToken = authTokenProvider.createAuthToken(userId, role, accessTokenExpiry);
        AuthToken refreshToken = authTokenProvider.createAuthToken(refreshTokenExpiry);

        // JWT 토큰을 쿠키에 저장
        CookieUtil.addCookie(response, "ACCESS_TOKEN", accessToken.getToken(), (int) (accessTokenExpiry.getTime() / 1000));
        CookieUtil.addCookie(response, "REFRESH_TOKEN", refreshToken.getToken(), (int) (refreshTokenExpiry.getTime() / 1000));

        log.info("Access Token: {}", accessToken.getToken());
        log.info("Refresh Token: {}", refreshToken.getToken());

        // 리다이렉트 URL 결정 및 리다이렉트 처리
        String targetUrl = determineTargetUrl(request, response, authentication);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        return UriComponentsBuilder.fromUriString("http://localhost:3000/post?category=전체")
                .build().encode().toUriString();
    }
}
