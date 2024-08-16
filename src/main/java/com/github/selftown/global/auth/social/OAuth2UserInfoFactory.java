package com.github.selftown.global.auth.social;

import com.github.selftown.domain.user.entity.Provider;

import java.util.Map;
import java.util.Objects;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(Provider provider, Map<String, Object> attributes) {
        if(Objects.requireNonNull(provider) == Provider.KAKAO) {
            return new KakaoOAuth2UserInfo(attributes);
        }
        throw new IllegalArgumentException("유효하지 않은 Provider Type입니다.");
    }
}
