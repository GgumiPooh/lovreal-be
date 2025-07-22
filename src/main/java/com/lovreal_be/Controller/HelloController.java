package com.lovreal_be.Controller;

import com.lovreal_be.Config.SecurityConfig;
import com.lovreal_be.Repository.MemberRepository;
import com.lovreal_be.domain.Member;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {
    private final MemberRepository memberRepository;
    private final SecurityConfig securityConfig;

    public HelloController(MemberRepository memberRepository, SecurityConfig securityConfig) {
        this.memberRepository = memberRepository;
        this.securityConfig = securityConfig;
    }

    @PostMapping("/lov")
    public void hello(@RequestBody MemberForm form) {
        BCryptPasswordEncoder bCryptPasswordEncoder = securityConfig.passwordEncoder();
        
        String id = form.getId();
        String password = form.getPassword();
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        String gender = form.getGender();

        Member member = new Member(id, encodedPassword, gender);

        memberRepository.save(member);
    }
}
