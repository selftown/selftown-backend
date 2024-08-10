package com.github.selftown.global.auth.OAuth.controller;
import com.github.selftown.global.auth.OAuth.dto.LoginResponse;
import com.github.selftown.global.auth.OAuth.service.OAuthService;
import com.github.selftown.global.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("${api_prefix}")
public class OAuthController {
    private final OAuthService oAuthService;

    @GetMapping("/auth/kakao")
    public ResponseEntity<ApiResponse<LoginResponse>> loginWithKaKao(@RequestParam String code) {
        LoginResponse loginResponse = oAuthService.kakaoLogin(code);

        // 로그인 성공 시와 실패 시 각각의 응답을 설정합니다.
        if (loginResponse.getToken() != null) {
            return ResponseEntity.ok(ApiResponse.<LoginResponse>builder()
                    .status(HttpStatus.OK.value())
                    .message("Login successful")
                    .data(loginResponse)
                    .build());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.<LoginResponse>builder()
                            .status(HttpStatus.UNAUTHORIZED.value())
                            .message("User not registered, redirect to signup")
                            .data(loginResponse)
                            .build());
        }

    }
}



