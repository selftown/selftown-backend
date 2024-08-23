package com.github.selftown.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    COOKIE_NOT_FOUND(404, "쿠키를 찾을 수 없습니다."),
    USER_NOT_FOUND(404, "해당 유저를 찾을 수 없습니다."),
    THRID_PARTY_ERROR(500, "써드 파티 인증 이용 중 오류가 발생하였습니다."),
    AUTH_INFO_NOT_FOUND(404, "인증 정보를 찾을 수 없습니다."),
    SOCIAL_ID_NOT_EQUAL(400, "소셜 아이디가 일치하지 않습니다."),
    TOKEN_NOT_VALID(400, "토큰이 유효하지 않습니다."),
    TOKEN_NOT_FOUND(404, "토큰데이터를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(500, "서버 내부 오류입니다."),
    INVALID_CATEGORY(400, "유효하지 않은 카테고리입니다.");

    private final int code;
    private final String message;
}
