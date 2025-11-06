package com.lovreal_be.security;
import com.lovreal_be.domain.Member;
import com.lovreal_be.repository.MemberRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String TOKEN_NAME = "jwt_token";
    @Value("${jwt.secret}")
    private String secret;
    private final SecretKey signingKey;
    private final MemberRepository memberRepository;

    // application.yml 예: jwt.secret: "3R2p9v... (Base64 인코딩 문자열)"
    public JwtTokenProvider(@Value("${jwt.secret}") String secret,
                            @Value("${jwt.expiration}") Duration accessTtl, MemberRepository memberRepository) {
        // Base64를 권장. (그냥 문자열이면 길이가 32바이트 이상인지 체크 필요)
        byte[] keyBytes;
        try {
            keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(secret);
        } catch (IllegalArgumentException ignore) {
            // Base64가 아니면 원문 바이트 사용 (단, 32바이트 이상이어야 함)
            keyBytes = secret.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        }
        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("jwt.secret must be at least 256 bits (32 bytes)");
        }
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.memberRepository = memberRepository;
    }

    public String generateToken(String memberString) {
        Instant now = Instant.now();
        Instant exp = now.plus(Duration.ofMinutes(30));

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(memberString)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public void addTokenToCookie(String token, HttpServletResponse  response) {
        ResponseCookie cookie = ResponseCookie.from(TOKEN_NAME, token)
                .httpOnly(true)
                .secure(false)
                .sameSite("None")
                .path("/")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        System.out.println("cookie = " + cookie);
//        Cookie cookie = new Cookie(TOKEN_NAME, token)
//                .setPath("/")
//                .setHttpOnly(true);
//        response.addCookie(cookie);
    }

    public Member getMemberFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        String memberString = claims.getSubject();
        return memberRepository.findByMemberString(memberString);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException e) {
            System.out.println("[JWT] invalid signature / weak key: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("[JWT] expired at " + e.getClaims().getExpiration());
        } catch (MalformedJwtException e) {
            System.out.println("[JWT] malformed: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("[JWT] unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("[JWT] empty token: " + e.getMessage());
        }
        return false;
    }
}
