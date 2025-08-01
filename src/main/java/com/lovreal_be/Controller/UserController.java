package com.lovreal_be.Controller;

import com.lovreal_be.DTO.CoupleResponse;
import com.lovreal_be.Repository.MemberRepository;
import com.lovreal_be.domain.Member;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api")
public class UserController {
    private final MemberRepository memberRepository;

    public UserController(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @GetMapping("/accept/couple")
    public ResponseEntity<?> acceptCouple(@CookieValue(value = "memberId") String memberId) {
        if (memberId == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }
        System.out.println("✅ /api/couple 컨트롤러 도착");

        Member member = memberRepository.findById(memberId).get();

        CoupleResponse response = new CoupleResponse(
                member.getId(),
                member.getCoupleId(),
                member.getCoupleRequest()
        );

        return ResponseEntity.ok(response);

    }

    @GetMapping("/couple")
    public ResponseEntity<?> couple(@CookieValue(value = "memberId") String memberId) {
        if (memberId == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }
        Member member = memberRepository.findById(memberId).get();

        CoupleResponse response = new CoupleResponse(
                member.getId(),
                member.getCoupleId(),
                member.getCoupleRequest()
        );
        return ResponseEntity.ok(response);
    }
    }
