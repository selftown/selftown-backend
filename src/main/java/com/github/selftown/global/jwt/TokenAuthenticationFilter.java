package com.github.selftown.global.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.HeaderUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final AuthTokenProvider authTokenProvider;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String authorizationHeader = request.getHeader("Authorization");
            String tokenStr = null;

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                tokenStr = authorizationHeader.substring(7);
            }

            if (tokenStr != null) {
                AuthToken token = authTokenProvider.convertAuthToken(tokenStr);

                if (token.validate()) {
                     Authentication authentication = authTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (ExpiredJwtException eje) {
            log.warn("JWT Token has expired: {}", eje.getMessage());
            request.setAttribute("expiredJwtException", eje);
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException e) {
            log.error("JWT Token is invalid: {}", e.getMessage());
            request.setAttribute("SecurityExceptionForJwt", e);
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument provided: {}", e.getMessage());
            request.setAttribute("IllegalArgumentException", e);
        } catch (Exception e) {
            log.error("An unexpected error occurred: {}", e.getMessage());
            request.setAttribute("Exception", e);
        }
        filterChain.doFilter(request, response);
    }
}
