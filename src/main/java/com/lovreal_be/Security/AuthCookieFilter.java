package com.lovreal_be.Security;

import com.lovreal_be.Service.CookieService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@AllArgsConstructor
public class AuthCookieFilter extends OncePerRequestFilter {


    private final CookieService cookieService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/public");
    }


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws ServletException, IOException {
        Optional<Cookie> cookieOpt = CookieUtil.getCookie(request, "SESSION_ID");
        System.out.println("asd" + cookieOpt.isPresent());
        if (cookieOpt.isPresent()) {
            String sessionId = cookieOpt.get().getValue();
            String memberId = cookieService.findMemberIdByCookieValue(sessionId);

            if (memberId != null) {
                System.out.println("memberId: " + memberId);
                request.setAttribute("memberId", memberId);
                chain.doFilter(request, response);
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired session");
    }
}
