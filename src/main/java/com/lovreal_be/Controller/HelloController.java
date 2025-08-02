package com.lovreal_be.Controller;

import com.lovreal_be.Config.SecurityConfig;
import com.lovreal_be.Repository.MemberRepository;
import com.lovreal_be.domain.Member;
import org.apache.commons.lang3.RandomStringUtils;
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
        BCryptPasswordEncoder bCryptPasswordEncoder = securityConfig.encoder();
        
        String id = form.getId();
        String password = form.getPassword();
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        String gender = form.getGender();

        Member member = new Member(id, encodedPassword, gender);

        memberRepository.save(member);
    }

    @PostMapping("/inviteCode")
    public void inviteCode(@RequestParam String id, @CookieValue(name = "memberId") String memberId) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(""));
        if (member == null) {

        }
        String inviteCode = RandomStringUtils.randomAlphanumeric(8);
    }
}
