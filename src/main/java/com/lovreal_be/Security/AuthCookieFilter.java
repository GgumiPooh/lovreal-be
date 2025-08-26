package com.lovreal_be.Security;

import com.lovreal_be.Service.SessionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@AllArgsConstructor
public class AuthCookieFilter extends OncePerRequestFilter {

    public static final String MEMBER_ID = "memberId";
    private final SessionService sessionService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/public");
    }


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain)
            throws ServletException, IOException {

        Optional<Cookie> cookieOpt = CookieUtil.getCookie(request, "JSESSIONID");
        if (cookieOpt.isPresent()) {
            String memberId = sessionService.findMemberIdByRequest(request);
            if (memberId != null) {
                chain.doFilter(request, response);
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired session");
    }
}
