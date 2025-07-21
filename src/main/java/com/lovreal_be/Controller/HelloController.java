package com.lovreal_be.Controller;

import com.lovreal_be.Config.SecurityConfig;
import com.lovreal_be.Repository.MemberRepository;
import com.lovreal_be.domain.Member;
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

        String securityPassword = securityConfig.passwordEncoder().encode(form.getPassword());

        Member member = new Member();
        member.setId(form.getId());
        member.setPassword(securityPassword);
        member.setGender(form.getGender());
        memberRepository.save(member);
        System.out.println(member);
    }

}