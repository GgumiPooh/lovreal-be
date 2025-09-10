package com.lovreal_be.controller;

import com.lovreal_be.repository.MemberRepository;
import com.lovreal_be.service.JwtService;
import com.lovreal_be.service.SessionService;
import com.lovreal_be.service.MemberServiceImpl;
import com.lovreal_be.domain.Member;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/couple")
    public String[] couple(HttpServletRequest request) {
        return memberServiceImpl.memberAndPartnerName(request);
    }

    @GetMapping("/profile")
    public String[] memberHome(HttpServletRequest request) {
        return memberServiceImpl.memberHome(request);
    }

    @GetMapping("/board")
    public ResponseEntity<?> board(HttpServletRequest request) {
        return memberServiceImpl.board(request);
    }


}


