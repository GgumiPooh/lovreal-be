package com.lovreal_be.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class BearerTokenResolver {
    public String resolve(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String prefix = "Bearer ";
        if (StringUtils.hasText(header) &&
                header.regionMatches(true, 0, prefix, 0, prefix.length())) {
            return header.substring(prefix.length()).trim();
        }
        return null;
    }
}