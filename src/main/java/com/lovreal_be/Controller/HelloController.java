package com.lovreal_be.Controller;

import com.lovreal_be.Config.SecurityConfig;
import com.lovreal_be.DTO.MemberForm;
import com.lovreal_be.Repository.MemberRepository;
import com.lovreal_be.Service.MemberService;
import com.lovreal_be.domain.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class HelloController {
    private final MemberRepository memberRepository;
    private final SecurityConfig securityConfig;
    private final MemberService memberService;

    @PostMapping("/public/signUp")
    public ResponseEntity<?> signUp(@RequestBody MemberForm form) {
        return memberService.signUp(form);
    }

    @PostMapping("/public/login")
    public ResponseEntity<?> login(@RequestBody MemberForm form, HttpServletResponse response) {
        System.out.println("login");
        return memberService.login(form, response);
    }


    @PostMapping("/member/inviteCode")
    public void inviteCode(HttpServletRequest request) {
        System.out.println("inviteCode");
    memberService.createInviteCode(request);
    }
}
