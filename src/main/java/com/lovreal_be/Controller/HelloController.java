
package com.lovreal_be.Controller;
import com.lovreal_be.DTO.CoupleRequest;
import com.lovreal_be.DTO.MemberForm;
import com.lovreal_be.Repository.MemberRepository;
import com.lovreal_be.Service.MemberService;
import com.lovreal_be.domain.Member;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
public class HelloController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    public HelloController(MemberService memberService, MemberRepository memberRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
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
    public ResponseEntity<?> login(@CookieValue(value = "memberId", required = false) String memberId, @RequestBody MemberForm form, HttpServletResponse response) {
        if(memberId != null) {
            return ResponseEntity.status(200).body("");
        }
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
    @PostMapping("/member/requestCouple")
    public ResponseEntity<?> requestCouple(@CookieValue(value = "memberId", required = false) String memberId,
                                    @RequestBody CoupleRequest coupleRequest) {
        if(memberId == null) {
            return ResponseEntity.status(402).body("회원정보가 없습니다");
        }
        String requestId = coupleRequest.getRequestCoupleId();
        if(requestId == null) {
            return ResponseEntity.status(404).body("존재하지 않는 회원입니다.");
        }
        System.out.println("requestId: " + requestId);
        Member member = memberRepository.findById(memberId).get();
        member.setCoupleRequest(requestId);
        System.out.println(member.getCoupleRequest());
        Member targetMember = memberRepository.findById(requestId).get();
        targetMember.setCoupleRequest(member.getId());


        memberRepository.save(member);
        memberRepository.save(targetMember);
        return ResponseEntity.status(200).body("커플 요청을 보냈습니다");
    }

    @PostMapping("/member/acceptCouple")
    public ResponseEntity<?> acceptCouple(@CookieValue(value = "memberId", required = false) String memberId) {
        if (memberId == null) {
            return ResponseEntity.status(402).body("회원정보가 없습니다");
        }
        Member member = memberRepository.findById(memberId).get();
        member.setCoupleId(member.getCoupleRequest());
        Member targetMember = memberRepository.findById(member.getCoupleRequest()).get();
        targetMember.setCoupleId(member.getId());
        memberRepository.save(targetMember);
        memberRepository.save(member);
        return ResponseEntity.status(200).body("커플이 되었습니다!");
    }

}