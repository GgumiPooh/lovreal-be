package com.lovreal_be.Controller;

import com.lovreal_be.DTO.CoupleDate;
import com.lovreal_be.DTO.MemberForm;
import com.lovreal_be.Service.MemberService;
import com.lovreal_be.Service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class HelloController {
    private final MemberService memberService;
    private final SessionService sessionService;

    @PostMapping("/public/signUp")
    public ResponseEntity<?> signUp(@ModelAttribute MemberForm form) {
        return memberService.signUp(form);
    }

    @PostMapping("/public/login")
    public ResponseEntity<?> login(@ModelAttribute MemberForm form, HttpServletResponse response, HttpServletRequest request) {
        return memberService.login(form, response, request);
    }

    @PostMapping("/member/inputInviteCode")
    public ResponseEntity<?> inputInviteCode(@RequestParam("inviteCode") String inviteCode, HttpServletRequest request) {
        return memberService.beCouple(inviteCode, request);
    }

    @PostMapping("/member/inviteCode")
    public void inviteCode(HttpServletRequest request) {
        memberService.createInviteCode(request);
    }

    @PostMapping("/member/coupleDate")
    public ResponseEntity<?> coupleDate(@RequestBody CoupleDate coupleDate, HttpServletRequest request) {
        return memberService.setBeCoupledDate(coupleDate, request);
    }

    @PostMapping("/member/profile")
    public ResponseEntity<?> profile(HttpServletRequest request) {
        if (sessionService.findMemberIdByRequest(request) == null) {
            return ResponseEntity.status(401).body("로그인해주세요.");
        }
        return ResponseEntity.ok().build();
    }


}