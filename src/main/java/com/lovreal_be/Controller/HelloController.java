package com.lovreal_be.Controller;
import com.lovreal_be.Service.MemberService;
import com.lovreal_be.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    private final MemberService memberService;

    public HelloController(MemberService memberService) {
        this.memberService = memberService;
    }


    @GetMapping("/idCheck")
    public ResponseEntity<?> idCheck(@RequestParam(name = "id") String id){
        return memberService.idDuplicateCheck(id);
    }

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody MemberForm form) {
        return memberService.signUp(form);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberForm form) {
        return memberService.login(form);
    }
}
