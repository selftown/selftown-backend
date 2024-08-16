package com.github.selftown.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    USER("ROLE_USER", "일반 사용자 권한"),
    ADMIN("ROLE_ADMIN","관리자 권한"),
    GUEST("ROLE_GUEST", "게스트 권한");

    private final String code;
    private final String displayName;
}
