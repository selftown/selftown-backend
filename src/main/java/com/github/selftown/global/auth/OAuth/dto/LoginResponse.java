package com.github.selftown.global.auth.OAuth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String token;
    private UserInfo userInfo;
}
