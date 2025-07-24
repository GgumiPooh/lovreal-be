package com.lovreal_be.Controller;
import com.lovreal_be.Service.MemberService;
import com.lovreal_be.domain.Member;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    private final MemberService memberService;

    public HelloController(MemberService memberService) {
        this.memberService = memberService;
    }


    @PostMapping("/signup")
    public void signup(@RequestBody MemberForm form) {
        String id = form.getId();
        String password = form.getPassword();
        String gender = form.getGender();
        Member member = new Member(id, password, gender);
        memberService.saveMember(member);
    }

    @PostMapping("/login")
    public void login(@RequestBody MemberForm form) {

    }
}
