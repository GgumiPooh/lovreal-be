package com.lovreal_be.security;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

//    private final SecretKey signingKey;
//    private final Duration accessTtl;


    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Duration accessTtl;

//    private final SecretKey signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
//    Duration accessTtl = jwtExpirationInMs;

//    public JwtTokenProvider(@Value("${jwt.secret}") String jwtSecret,  @Value("${jwt.expiration}") Duration jwtExpirationInMs) {
//        this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
//        this.accessTtl = jwtExpirationInMs;
//
//    }

    public String generateToken(String memberId) {
        // Assuming UserDetails is implemented by your User class
        Map<String,Object> header = new HashMap<>();
        header.put("typ", "JWT");

        Instant now = Instant.now();
        Instant exp = now.plus(accessTtl);
        Map<String, Object> payload = new HashMap<>();
        payload.put("memberId", memberId);

        return Jwts.builder()
                .setHeader(header)
                .setClaims(payload)
                .setSubject(memberId)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(Keys.hmacShaKeyFor(jwtSecret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getMemberIdFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            System.out.println("authToken = " + authToken);
            Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes())).build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            System.out.println("a : " + ex.getMessage());
            // Invalid JWT token
        } catch (ExpiredJwtException ex) {
            System.out.println("b : " +ex.getMessage());
            // Expired JWT token
        } catch (UnsupportedJwtException ex) {
            System.out.println("c : " +ex.getMessage());
            // Unsupported JWT token
        } catch (IllegalArgumentException ex) {
            System.out.println("d : " +ex.getMessage());
            // JWT claims string is empty
        }
        return false;
    }
}