package com.lahee.mutsasns.jwt;

import com.lahee.mutsasns.dto.user.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenUtils {
    //jwt 관련 기능을 넣어두기 위한
    private Key signinKey;
    private final JwtParser jwtParser;
    private final String jwtSecret;
    private final int accessExpirationTime;

    public JwtTokenUtils(@Value("${jwt.secret}") String jwtSecret, @Value("${jwt.accessExpirationTime}") int accessExpirationTime) {
        log.info("jwt secret : {} ", jwtSecret);
        log.info("expiration time : {}", accessExpirationTime);
        this.accessExpirationTime = accessExpirationTime;
        this.jwtSecret = jwtSecret;
        this.signinKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        this.jwtParser = Jwts.parserBuilder().setSigningKey(jwtSecret).build();
    }

    //주어진 사용자 정보를 바탕으로 JWT를 문자열로 생성한다.
    public TokenDto generateToken(UserDetails userDetails) { //토큰 발급
        Date expiredTime = Date.from(Instant.now().plusSeconds(accessExpirationTime));
        String accessToken = Jwts.builder()
                .setClaims(Jwts.claims()
                        .setSubject(userDetails.getUsername())
                        .setIssuedAt(Date.from(Instant.now()))
                        .setExpiration(expiredTime))
                .signWith(signinKey)
                .compact();

        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .accessTokenExpiresIn(expiredTime.getTime())
                .build();
    }
    public Authentication getAuthentication(String accessToken) {

        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(signinKey)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();

        User principal = new User(claims.getSubject(), "", new ArrayList<>());
        return new UsernamePasswordAuthenticationToken(principal, accessToken, new ArrayList<>());
    }

    public boolean validateJwt(String token) {
        try {
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
    //인증 정보 파싱

    public Claims parseClaims(String token) {
        try {
            return jwtParser.parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}