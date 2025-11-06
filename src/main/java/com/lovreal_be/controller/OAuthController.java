package com.lovreal_be.controller;

import com.lovreal_be.domain.Member;
import com.lovreal_be.domain.OAuthMember;
import com.lovreal_be.security.JwtTokenProvider;
import com.lovreal_be.security.TokenResolver;
import com.lovreal_be.service.JwtService;
import com.lovreal_be.service.MemberServiceImpl;
import com.lovreal_be.service.NaverAuthService;
import com.lovreal_be.service.NaverProfileClient;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class OAuthController {
    private final NaverProfileClient naverProfileClient;
    private final JwtService jwtService;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenResolver tokenResolver;
    private final MemberServiceImpl memberServiceImpl;
    String afterUrl =  "http://localhost:5173/member/recieveOrSend";
    private final NaverAuthService naverAuthService;

    public OAuthController(NaverAuthService naverAuthService, NaverProfileClient naverProfileClient, JwtService jwtService, JwtTokenProvider jwtTokenProvider, TokenResolver tokenResolver, MemberServiceImpl memberServiceImpl) {
        this.naverAuthService = naverAuthService;
        this.naverProfileClient = naverProfileClient;
        this.jwtService = jwtService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenResolver = tokenResolver;
        this.memberServiceImpl = memberServiceImpl;
    }

    @GetMapping("/api/public/auth/naver/login")
    public void NaverLogin(HttpServletResponse response) throws IOException {
       NaverAuthService.AuthRedirect authRedirect = naverAuthService.createAuthorizeUrl();
       String state = authRedirect.state();
        Cookie c = new Cookie("naver_state", state);
        c.setHttpOnly(true);
        c.setSecure(true);
        c.setPath("/");
        c.setMaxAge(300); // 5분
        response.addCookie(c);
        response.sendRedirect(authRedirect.redirectUri());
    }

    @GetMapping("/api/public/auth/naver/callback")
    public ResponseEntity<String> NaverCallback(
            @RequestParam String code, @RequestParam String state, @CookieValue(name = "naver_state", required=false) String cookieState,
            HttpServletResponse response, HttpServletRequest request
    ) throws IOException {
        if (cookieState == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        System.out.println("code: " + code);
        // 1) state 검증 (프론트에서 보냈던 random과 같은지)
        if (!state.equals(cookieState)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // 2) code로 토큰 요청
        String accessToken = naverAuthService.exchangeToken(code, state).access_token();
        System.out.println("accessToken = " + accessToken);
        //access token 저장 & jwt발급?
        Member member = naverAuthService.findOrCreateMember(accessToken);
        String jwtToken = jwtTokenProvider.generateToken(member.getMemberString());
        jwtTokenProvider.addTokenToCookie(jwtToken, response);

        System.out.println("jwtToken = " + jwtToken);
        response.sendRedirect(afterUrl);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
