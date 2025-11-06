package com.lovreal_be.security;

import jakarta.servlet.http.HttpServletRequest;

public interface TokenResolver {
    public String resolve(HttpServletRequest request);
}
