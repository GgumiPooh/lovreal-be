package com.lovreal_be.controller;

import com.lovreal_be.DTO.CoupleDate;
import com.lovreal_be.DTO.StoryForm;
import com.lovreal_be.domain.Member;
import com.lovreal_be.security.BearerTokenResolver;
import com.lovreal_be.security.JwtTokenProvider;
import com.lovreal_be.service.JwtService;
import com.lovreal_be.service.MemberServiceImpl;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final BearerTokenResolver bearerTokenResolver;
    private final JwtService jwtService;

    @GetMapping("/api/member/status-check")
    public ResponseEntity<String> memberStatusCheck(@CookieValue(name = "jwt_token")  String jwtToken) {
        System.out.println("aaaaaaaaaaaaaaaaa");
        Member member = jwtTokenProvider.getMemberFromJWT(jwtToken);
        System.out.println("member.getNickname() = " + member.getNickname());
        return memberServiceImpl.memberStatusCheck(member);
    }

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
    public void beCoupleDay(@RequestBody CoupleDate coupleDate, @CookieValue(name = "jwt_token") String token, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("coupleDate = " + coupleDate);
        memberServiceImpl.setBeCoupledDay(coupleDate, jwtTokenProvider.getMemberFromJWT(token), request, response);
    }

    @PostMapping("/profile")
    public ResponseEntity<?> profile(HttpServletRequest request) {
        if (jwtService.getTokenAndFindMember(request) == null) {
            return ResponseEntity.status(401).body("로그인해주세요.");
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/new-story")
    public void newStory(@CookieValue(name = "jwt_token") String jwtToken, @ModelAttribute StoryForm storyForm, HttpServletResponse response, HttpServletRequest request) throws IOException {
        System.out.println("storyForm = " + storyForm);
        memberServiceImpl.writeNewStory(storyForm,response, jwtToken, request);
    }
}


