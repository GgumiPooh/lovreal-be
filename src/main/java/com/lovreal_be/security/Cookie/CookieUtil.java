package com.lovreal_be.security.Cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CookieUtil {
    public Cookie createCookie(String userName) {
        Cookie cookie = new Cookie("memberId", userName);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);
        return cookie;
    }

    public void deleteCookie(HttpServletRequest request, HttpServletResponse response, String id) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if(!cookie.getName().equals(id)) {
                    cookie.setValue("");            // 빈 값으로 설정
                    cookie.setMaxAge(0);            // 즉시 만료
                    cookie.setPath("/");            // 경로도 꼭 지정해야 함
                    response.addCookie(cookie);     // 변경된 쿠키 다시 보내기
                }

            }
        }
    }


}
