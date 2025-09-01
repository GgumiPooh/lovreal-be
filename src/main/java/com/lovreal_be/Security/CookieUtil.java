package com.lovreal_be.Security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@UtilityClass
public class CookieUtil {

    public String createCookie(HttpServletResponse response) {
        String cookieValue = UUID.randomUUID().toString();
        Cookie cookie = new Cookie("SESSIONID" , cookieValue);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); // 1일
        response.addCookie(cookie);
        return cookieValue;
    }

    public void deleteCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public Optional<Cookie> getCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) return Optional.empty();
        return Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(cookieName))
                .findFirst();
    }
}
