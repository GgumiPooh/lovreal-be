package com.lovreal_be.controller;

import com.lovreal_be.DTO.MemberForm;
import com.lovreal_be.service.JwtService;
import com.lovreal_be.service.MemberServiceImpl;
import com.lovreal_be.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public")
public class PublicController {
    private final MemberServiceImpl memberServiceImpl;
    private final SessionService sessionService;
    private final JwtService jwtService;

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@ModelAttribute MemberForm form) {
        return memberServiceImpl.signUp(form);
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberForm form) {
        try {
            return jwtService.login(form);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@ModelAttribute MemberForm form, HttpServletResponse response, HttpServletRequest request) {
//        return memberServiceImpl.login(form, response, request);
//    }
}