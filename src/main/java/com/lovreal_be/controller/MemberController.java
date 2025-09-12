package com.lovreal_be.controller;

import com.lovreal_be.DTO.CoupleDate;
import com.lovreal_be.DTO.StoryForm;
import com.lovreal_be.domain.Member;
import com.lovreal_be.security.BearerTokenResolver;
import com.lovreal_be.security.JwtTokenProvider;
import com.lovreal_be.service.JwtService;
import com.lovreal_be.service.MemberServiceImpl;
import com.lovreal_be.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
    private final MemberServiceImpl memberServiceImpl;
    private final SessionService sessionService;
    private final JwtTokenProvider jwtTokenProvider;
    private final BearerTokenResolver bearerTokenResolver;
    private final JwtService jwtService;

    @PostMapping("/input-invite-code")
    public ResponseEntity<String> inputInviteCode(@RequestParam("inviteCode") String inviteCode, HttpServletRequest request) {
        try {
            Member member = jwtService.getTokenAndFindMember(request);
            return memberServiceImpl.beCouple(inviteCode, member);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/invite-code")
    public ResponseEntity<String> inviteCode(HttpServletRequest request) {
            Member member = jwtService.getTokenAndFindMember(request);
            System.out.println(member.getId());
            return memberServiceImpl.createInviteCode(member);
    }

    @PostMapping("/couple-date")
    public void beCoupleDay(@RequestBody CoupleDate coupleDate, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("coupleDate = " + coupleDate);
        memberServiceImpl.setBeCoupledDay(coupleDate, request, response);
    }

    @PostMapping("/profile")
    public ResponseEntity<?> profile(HttpServletRequest request) {
        if (sessionService.findMemberIdByRequest(request) == null) {
            return ResponseEntity.status(401).body("로그인해주세요.");
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/new-story")
    public void newStory(@ModelAttribute StoryForm storyForm, HttpServletResponse response, HttpServletRequest request) throws IOException {
        System.out.println("storyForm = " + storyForm);
        memberServiceImpl.writeNewStory(storyForm,response, request);
    }
}


