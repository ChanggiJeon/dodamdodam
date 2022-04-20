package com.ssafy.api.config.jwt;

import com.ssafy.api.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt_token.expiration_time}")
    private static int JWT_TOKEN_EXPIRATION_TIME;
    @Value("${refresh_token.expiration_time}")
    private static int REFRESH_TOKEN_EXPIRATION_TIME;

    private final UserDetailsService userDetailsService;

    // Jwt 토큰 생성
    public String createAccessToken(User user) {
        Claims claims = Jwts.claims().setSubject(user.getUsername());
        Pair<String, Key> key = JwtKey.getRandomKey();
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 데이터
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(new Date(now.getTime() + JWT_TOKEN_EXPIRATION_TIME)) // set Expire Time
                .setHeaderParam(JwsHeader.KEY_ID, key.getFirst()) // kid
                .signWith(key.getSecond()) // signature
                .compact();
    }

    // 로그인시 RefreshToken 발급
    public String createRefreshToken() {
        Pair<String, Key> key = JwtKey.getRandomKey();
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_TIME)) // set Expire Time
                .setHeaderParam(JwsHeader.KEY_ID, key.getFirst()) // kid
                .signWith(key.getSecond()) // signature
                .compact();
    }

    // Jwt 토큰에서 회원 구별 정보 추출
    public String getUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKeyResolver(SigningKeyResolver.instance)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Jwt 토큰으로 인증 정보를 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Jwt 토큰의 유효성 + 만료일자 확인
    public boolean validateToken(String jwtToken) {
        System.out.println(new Date());
        try {
            Jws<Claims> claims = Jwts.parserBuilder().build().parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String resolveToken(HttpServletRequest req) {
        return req.getHeader("X-AUTH-TOKEN");
    }
}
