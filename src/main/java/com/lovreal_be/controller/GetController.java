package com.lovreal_be.controller;

import com.lovreal_be.DTO.StoryForm;
import com.lovreal_be.repository.MemberRepository;
import com.lovreal_be.service.JwtService;
import com.lovreal_be.service.SessionService;
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
    private final SessionService sessionService;
    private final MemberRepository memberRepository;
    private final JwtService jwtService;


    @GetMapping("/invite-code")
    public String inviteCode(HttpServletRequest request) {
        Member member = jwtService.getTokenAndFindMember(request);
        return member.getInviteCode();
    }

    @GetMapping("/couple-date")
    public String[] coupleDate(HttpServletRequest request) {
        return memberServiceImpl.memberAndPartnerName(request);
    }

    @GetMapping("/profile")
    public String[] memberHome(HttpServletRequest request) {
        return memberServiceImpl.memberHome(request);
    }

    @GetMapping("/board")
    public List<StoryForm> board(HttpServletRequest request, HttpServletResponse response) {
        return memberServiceImpl.board(request, response);
    }

    @GetMapping("/d-day")
    public Long dDay(HttpServletRequest request) {
        return memberServiceImpl.dDayCalculator(request);
    }

    @GetMapping("/profile-img")
    public String profileImg(HttpServletRequest request) {
        System.out.println("profileImg");
        return memberServiceImpl.memberProfileImg(request);
    }


}


