package com.lahee.mutsasns.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final JwtTokenUtils jwtTokenUtils;

    public JwtTokenFilter(JwtTokenUtils jwtTokenUtils) {
        this.jwtTokenUtils = jwtTokenUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String token = resolveToken(request);
        log.info("accessToken {}", token);

        try {
            if (StringUtils.hasText(token) && jwtTokenUtils.validateJwt(token)) {
                Authentication authentication = jwtTokenUtils.getAuthentication(token);

                SecurityContext context = SecurityContextHolder.createEmptyContext();
                String username = jwtTokenUtils.parseClaims(token).getSubject();

                context.setAuthentication(authentication);
                SecurityContextHolder.setContext(context);

                log.info("인증 정보를 저장했습니다 {}", username);
            } else {
                log.info("access token에 에러가 있습니다.");
            }
        } catch (ExpiredJwtException e) {
            log.info("만료된 Access 토큰입니다.");
        }

        chain.doFilter(request, response);
    }


    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.split(" ")[1].trim();
        }
        return null;
    }
}
