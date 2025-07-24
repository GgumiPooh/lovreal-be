package com.lovreal_be.Controller;

import com.lovreal_be.Config.SecurityConfig;
import com.lovreal_be.DTO.MemberForm;
import com.lovreal_be.Repository.MemberRepository;
import com.lovreal_be.Service.MemberService;
import com.lovreal_be.domain.Member;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
public class HelloController {
    private final MemberService memberService;

    public HelloController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/signup")
    public void singUp(@RequestBody MemberForm form) {

    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberForm form) {
        String id = form.getId();
        String password = form.getPassword();

        return memberService.login(id, password);
    }

}