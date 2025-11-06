package com.lovreal_be.controller;

import com.lovreal_be.DTO.StoryForm;
import com.lovreal_be.repository.MemberRepository;
import com.lovreal_be.security.JwtTokenProvider;
import com.lovreal_be.service.JwtService;
import com.lovreal_be.service.MemberServiceImpl;
import com.lovreal_be.domain.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/member")
@AllArgsConstructor
public class GetController {
    private final MemberServiceImpl memberServiceImpl;
    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final JwtTokenProvider jwtTokenProvider;


    @GetMapping("/invite-code")
    public String inviteCode(@CookieValue(name = "jwt_token") String token,HttpServletRequest request) {
        return jwtTokenProvider.getMemberFromJWT(token).getInviteCode();
    }

    @GetMapping("/couple-date")
    public String[] coupleDate(@CookieValue(name = "jwt_token") String token, HttpServletRequest request) {

        return memberServiceImpl.memberAndPartnerName(jwtTokenProvider.getMemberFromJWT(token));
    }

    @GetMapping("/profile")
    public String[] memberHome(@CookieValue(name = "jwt_token") String token, HttpServletRequest request) {
        return memberServiceImpl.memberHome(jwtTokenProvider.getMemberFromJWT(token));
    }

    @GetMapping("/board")
    public List<StoryForm> board(@CookieValue(name = "jwt_token") String token, HttpServletRequest request, HttpServletResponse response) {
        return memberServiceImpl.board(jwtTokenProvider.getMemberFromJWT(token), request, response);
    }

    @GetMapping("/d-day")
    public Long dDay(@CookieValue(name = "jwt_token") String token, HttpServletRequest request) {
        return memberServiceImpl.dDayCalculator(jwtTokenProvider.getMemberFromJWT(token));
    }

    @GetMapping("/profile-img")
    public String profileImg(@CookieValue(name = "jwt_token") String token, HttpServletRequest request) {
        System.out.println("profileImg");
        return memberServiceImpl.memberProfileImg(jwtTokenProvider.getMemberFromJWT(token));
    }


}


