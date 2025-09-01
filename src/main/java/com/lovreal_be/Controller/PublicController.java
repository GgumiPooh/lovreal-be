package com.lovreal_be.Controller;

import com.lovreal_be.DTO.MemberForm;
import com.lovreal_be.Service.MemberService;
import com.lovreal_be.Service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public")
public class PublicController {
    private final MemberService memberService;
    private final SessionService sessionService;

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@ModelAttribute MemberForm form) {
        return memberService.signUp(form);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@ModelAttribute MemberForm form, HttpServletResponse response, HttpServletRequest request) {
        return memberService.login(form, response, request);
    }
}