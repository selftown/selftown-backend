package com.github.selftown.global.auth.social;

public interface OAuth2UserInfo {
    String getId();
    String getName();
    String getEmail();
    String getProfileImage();
}
