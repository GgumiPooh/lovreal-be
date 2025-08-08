package com.lovreal_be.Controller;

import com.lovreal_be.Config.SecurityConfig;
import com.lovreal_be.DTO.MemberForm;
import com.lovreal_be.Repository.MemberRepository;
import com.lovreal_be.Service.CookieService;
import com.lovreal_be.Service.MemberService;
import com.lovreal_be.domain.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class HelloController {
    private final MemberRepository memberRepository;
    private final SecurityConfig securityConfig;
    private final MemberService memberService;
    private final CookieService cookieService;

    @PostMapping("/public/signUp")
    public ResponseEntity<?> signUp(@ModelAttribute MemberForm form) {
        return memberService.signUp(form);
    }

    @PostMapping("/public/login")
    public ResponseEntity<?> login(@ModelAttribute MemberForm form, HttpServletResponse response, HttpServletRequest request) {
        System.out.println("login");
        return memberService.login(form, response, request);
    }

    @PostMapping("/member/inputInviteCode")
    public ResponseEntity<?> inputInviteCode(@RequestParam("inviteCode") String inviteCode, HttpServletRequest request) {
        return  memberService.coupleRequest(inviteCode, request);
    }

    @PostMapping("/member/inviteCode")
    public void inviteCode(HttpServletRequest request) {
        System.out.println("inviteCode");
        String memberId = cookieService.findMemberIdByRequest(request);
        Member member = memberRepository.findById(memberId).orElse(null);
        if(member != null && member.getInviteCode() == null) {
            memberService.createInviteCode(member);
        }
    }



}
