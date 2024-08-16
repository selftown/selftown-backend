package com.github.selftown.global.service;

import com.github.selftown.domain.user.entity.AuthInfo;
import com.github.selftown.domain.user.entity.Provider;
import com.github.selftown.domain.user.entity.Role;
import com.github.selftown.domain.user.entity.User;
import com.github.selftown.domain.user.repository.AuthInfoRepository;
import com.github.selftown.domain.user.repository.UserRepository;
import com.github.selftown.global.auth.PrincipalDetails;
import com.github.selftown.global.auth.social.OAuth2UserInfo;
import com.github.selftown.global.auth.social.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final AuthInfoRepository authInfoRepository;
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        try {
            return this.loadOrRegisterUser(userRequest, user);
        } catch(AuthenticationException ex) {
            throw ex;
        } catch(Exception ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage());
        }
    }

    private OAuth2User loadOrRegisterUser(OAuth2UserRequest userRequest, OAuth2User user) {
        Provider provider = Provider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, user.getAttributes());

        String userSocialId = userInfo.getId();

        User savedUser = userRepository.findBySocialIdAndProvider(userSocialId, provider)
                .orElseGet(() -> createAuthAndUser(userInfo, provider));

        return PrincipalDetails.create(savedUser, userSocialId, provider, user.getAttributes());

    }

    private User createAuthAndUser(OAuth2UserInfo userInfo, Provider provider) {
        AuthInfo authInfo = AuthInfo.builder()
                .socialId(userInfo.getId())
                .provider(provider)
                .build();
        authInfoRepository.save(authInfo);

        User user = User.builder()
                .authInfo(authInfo)
                .email(userInfo.getEmail())
                .profileImage(userInfo.getProfileImage())
                .name(userInfo.getName())
                .role(Role.USER)
                .build();
        userRepository.save(user);

        return user;
    }
}
