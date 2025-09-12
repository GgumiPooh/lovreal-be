package com.lovreal_be.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Component
public class JwtTokenProvider {

    private final SecretKey signingKey;

    // application.yml 예: jwt.secret: "3R2p9v... (Base64 인코딩 문자열)"
    public JwtTokenProvider(@Value("${jwt.secret}") String secret,
                            @Value("${jwt.expiration}") Duration accessTtl) {
        this.accessTtl = accessTtl;
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
    }

    private final Duration accessTtl;

    public String generateToken(String memberId) {
        Instant now = Instant.now();
        Instant exp = now.plus(accessTtl);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(memberId)
                .claim("memberId", memberId)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getMemberIdFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
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
