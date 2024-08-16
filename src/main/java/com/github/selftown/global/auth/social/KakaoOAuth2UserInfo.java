package com.github.selftown.global.auth.social;

import java.util.Map;

public class KakaoOAuth2UserInfo implements OAuth2UserInfo{
    private final Map<String, Object> attributes;

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getId() {
        return getProperty("id");
    }

    @Override
    public String getName() {
        return getProperty("properties", "nickname");
    }

    @Override
    public String getEmail() {
        return getProperty("kakao_account", "email");
    }

    @Override
    public String getProfileImage() {
        return getProperty("properties", "profile_image");
    }

    private String getProperty(String key) {
        return String.valueOf(attributes.get(key));
    }

    private String getProperty(String key1, String key2) {
        Map<String, Object> target = (Map<String, Object>) attributes.get(key1);
        return target != null ? String.valueOf(target.get(key2)) : null;
    }

}
