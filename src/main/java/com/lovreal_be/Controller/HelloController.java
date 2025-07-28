package com.lovreal_be.Controller;
import com.lovreal_be.Service.MemberService;
import com.lovreal_be.domain.Member;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    private final MemberService memberService;

    public HelloController(MemberService memberService) {
        this.memberService = memberService;
    }


//    @PostMapping("/idCheck")
//    public ResponseEntity<?> idCheck(@RequestParam(name = "id") String id){
//        return memberService.idDuplicateCheck(id);
//    }

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody MemberForm form) {
        return memberService.signUp(form);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberForm form, HttpServletResponse response) {
        return memberService.login(form, response);
    }

    @PostMapping("/cookie")
    public String cookie(HttpServletRequest request) {
        String[] result;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userName".equals(cookie.getName())) {
                    String value = cookie.getValue();
                    System.out.println("username 쿠키 값: " + value);
                }
            }
        }
        return null;
    }
}
