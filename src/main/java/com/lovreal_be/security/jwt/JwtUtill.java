package com.lovreal_be.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Date;

public class JwtUtill {
    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(String username) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

        long EXPIRATION = 60 * 60 * 1000; //1시간
        return Jwts.builder().setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS512).compact();
    }

    public Claims getAllClaims(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    public boolean validateToken(String token) {
        try {
            getAllClaims(token);
            return true;
        } catch (JwtException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public  String getUsername(String token) {
        return getAllClaims(token).getSubject();
    }
}
