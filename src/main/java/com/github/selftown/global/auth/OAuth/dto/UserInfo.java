package com.github.selftown.global.auth.OAuth.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfo {
    private String nickname;
    private String profileImage;
    private String email;
    private Long id;
}
