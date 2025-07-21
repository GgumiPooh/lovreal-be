package com.lovreal_be.Controller;

import com.lovreal_be.Repository.MemberRepository;
import com.lovreal_be.domain.Member;
import org.springframework.web.bind.annotation.*;



@RestController
public class HelloController {

    private final MemberRepository memberRepository;

    public HelloController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    @PostMapping("/love")
    public void hello(@RequestBody MemberForm form) {

        Member member = new Member();
        member.setId(form.getId());
        member.setPassword(form.getPassword());
        member.setGender(form.getGender());
        memberRepository.save(member);
        System.out.println(member);
    }

}