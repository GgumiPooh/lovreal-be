package com.lovreal_be.Controller;

import com.lovreal_be.Repository.MemberRepository;
import com.lovreal_be.Service.CookieService;
import com.lovreal_be.Service.MemberService;
import com.lovreal_be.domain.Member;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/member")
@AllArgsConstructor
public class GetController {
    private final MemberService memberService;
    private final CookieService cookieService;
    private final MemberRepository memberRepository;


    @GetMapping("/inviteCode")
    public String inviteCode(HttpServletRequest request) {
        String memberId = cookieService.findMemberIdByRequest(request);
        Member member = memberRepository.findById(memberId).orElse(null);
        if (member == null) {
            return null;
        }
        return member.getInviteCode();
    }

    @GetMapping("/couple")
    public String[] couple(HttpServletRequest request) {
        return memberService.memberAndPartnerName(request);
    }

    @GetMapping("/profile")
    public String[] memberHome(HttpServletRequest request) {
        return memberService.memberHome(request);
    }


}


