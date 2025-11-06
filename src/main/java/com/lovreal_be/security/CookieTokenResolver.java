package com.lovreal_be.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

public class CookieTokenResolver implements TokenResolver {
    public String resolve(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt_token".equals(cookie.getName()) &&
                        StringUtils.hasText(cookie.getValue())) {
                    return cookie.getValue().trim();
                }
            }
        }
        return null;
    }
}
