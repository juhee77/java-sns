package com.lahee.mutsasns.config.security;

import com.lahee.mutsasns.jwt.JwtTokenFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.stream.Stream;



@Configuration
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;

    private static final String[] PERMIT_ALL_PATTERNS = new String[] {
            "/v3/api-docs/**",
            "/configuration/**",
            "/swagger*/**",
            "/webjars/**",
            "/swagger-ui/**",
            "/docs",
            "/static/**",
            "/api/user/auth/**",
    };

    @Bean //메서드의 결과를 bean 객체로 등록해주는 어노테이션
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AccessDeniedHandler ad, AuthenticationEntryPoint ap, CorsFilter cf) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) //cross site request 방지를 위한 추가(실제 배포 시에는 넣지 않는게 좋다)
                .headers(x -> x.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .addFilterBefore(cf, UsernamePasswordAuthenticationFilter.class) //유저 이름과 패스워드 검증 확인
                .addFilterBefore(jwtTokenFilter, AuthorizationFilter.class)

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        authHttp -> authHttp
                                .requestMatchers( //인증 관련 정보만 추가
                                        Stream.of(PERMIT_ALL_PATTERNS)
                                                .map(AntPathRequestMatcher::antMatcher)
                                                .toArray(AntPathRequestMatcher[]::new)
                                        //리엑트 관련 view만 분리해서 작성(이후에 배포시에는 포트를 다르게 하는 방식을 적용해야 할듯
                                )
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .exceptionHandling(e -> e.accessDeniedHandler(ad).authenticationEntryPoint(ap));
        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, e) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            response.setContentType("text/plain;charset=UTF-8");
//            response.getWriter().write("ACCESS DENIED");
//            response.getWriter().flush();
//            response.getWriter().close();
        };
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, e) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.setContentType("text/plain;charset=UTF-8");
//            response.getWriter().write("UNAUTHORIZED");
//            response.getWriter().flush();
//            response.getWriter().close();
        };
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
