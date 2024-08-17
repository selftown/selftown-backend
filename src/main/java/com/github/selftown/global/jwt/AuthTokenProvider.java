package com.github.selftown.global.jwt;

import com.auth0.jwt.interfaces.Claim;
import com.github.selftown.domain.user.entity.Role;
import com.github.selftown.global.config.AppProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthTokenProvider {
    private static final String LOCAL_USER_ID = "userId";
    private static final String AUTHORITIES_KEY = "role";

    private final AppProperties appProperties;
    private Key key;

    @Autowired
    public AuthTokenProvider(AppProperties appProperties) {
        this.appProperties = appProperties;
        this.key = Keys.hmacShaKeyFor(this.appProperties.getAuth().getTokenSecret().getBytes());
    }

    public AuthToken createAuthToken(Date expiry) {
        return new AuthToken(expiry, key);
    }

    public AuthToken createAuthToken(Long userId, String role, Date expiry) {
        return new AuthToken(userId, role, expiry, key);
    }

    public AuthToken convertAuthToken(String token) {
        return new AuthToken(token, key);
    }

    public Authentication getAuthentication(AuthToken authToken) {
        // 1. 토큰 유효성 검사
        if (authToken.validate()) {
            // 2. 토큰이 유효하면 클에임 정보를 가져옴
            Claims claims = authToken.getTokenClaims();
            Collection<? extends GrantedAuthority> authorities;

            // 3. 클레임에 AUTHORITIES_KEY가 존재하는지 확인
            if (claims.get(AUTHORITIES_KEY) != null) {
                // 4. AUTHORITIES_KEY가 있으면 이를 권한 리스트로 변환
                authorities =
                        Arrays.stream(new String[]{claims.get(AUTHORITIES_KEY).toString()})
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList());

                // 5. 클레임에서 추출한 정보를 사용하여 UserDetails 작성
                UserDetails principal = new User(claims.getSubject(), "", authorities);
                // 6. 인증 토큰 생성 및 반환 (비밀번호는 빈 문자열, 권한 정보 포함)
                return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);

            } else {
                // 7. AUTHORITIES_KEY가 없으면 기본적으로 게스트 권한 부여
                SimpleGrantedAuthority guestAuthority = new SimpleGrantedAuthority(Role.GUEST.getCode());

                authorities = Collections.singletonList(guestAuthority);
                // 8. 사용자 이름을 "anonymous"로 설정하고, 게스트 권한을 부여하여 UserDetails 객체 생
                UserDetails principal = new User("anonymous", "", authorities);
                // 9. 인증 토큰 생성 및 반환 (비밀번호와 인증 토큰은 null, 권한 정보 포함)
                return new UsernamePasswordAuthenticationToken(principal, null, authorities);

            }
        } else {
            // 10. 토큰이 유효하지 않으면 게스트 권한 부여
            SimpleGrantedAuthority guestAuthority = new SimpleGrantedAuthority(Role.GUEST.getCode());
            Collection<? extends GrantedAuthority> authorities = Collections.singleton(guestAuthority);

            UserDetails principal = new User("anonymous", "", authorities);
            return new UsernamePasswordAuthenticationToken(principal, null, authorities);
        }
    }
}
