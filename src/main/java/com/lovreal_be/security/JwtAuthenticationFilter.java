package com.lovreal_be.security;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, UserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        //PREFLIGHT 요청은 항상 OPTION 메소를 이용
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            System.out.println("request = " + request);
            return true;
        }
        String path = request.getRequestURI();
        return path.startsWith("/api/public");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            System.out.println("HTTP Method: " + request.getMethod());
            String jwt = getJwtFromRequest(request);
            System.out.println("jwt: " + jwt);
            System.out.println("StringUtils.hasText(jwt): " + StringUtils.hasText(jwt));
            System.out.println("validateToken: "+tokenProvider.validateToken(jwt));
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                String memberId = tokenProvider.getMemberIdFromJWT(jwt);
                System.out.println("memberId: " + memberId);
                UserDetails userDetails = userDetailsService.loadUserByUsername(memberId);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("authentication = " + authentication);
            }
        } catch (Exception ex) {
            System.out.println("ex = " + ex);
            // Log the exception
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}