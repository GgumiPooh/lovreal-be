package com.lovreal_be.Service;

import com.lovreal_be.Config.SecurityConfig;
import com.lovreal_be.DTO.MemberForm;
import com.lovreal_be.Repository.MemberRepository;
import com.lovreal_be.domain.Member;
import com.lovreal_be.security.Cookie.CookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final SecurityConfig securityConfig;
    private final CookieUtil cookieUtil;

    public ResponseEntity<?> idDuplicateCheck(String id) {
        if (memberRepository.findById(id).isPresent()) {
            System.out.println("fail");
            return ResponseEntity.status(409).body(Map.of("message", "이미 존재하는 아이디입니다."));
        } else {
            System.out.println("success");
            return ResponseEntity.ok(Map.of("message", "사용가능한 아이디입니다."));
        }
    }

    public ResponseEntity<?> signUp(MemberForm form) {
        try {
            String id = form.getId();
            String password = form.getPassword();
            String encodedPassword = securityConfig.encoder().encode(password);
            String gender = form.getGender();
            Member member = new Member(id, encodedPassword, gender);
            memberRepository.save(member);
            return ResponseEntity.status(200).body("회원가입 완료");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body("에러");
        }

    }

    public ResponseEntity<?> login(MemberForm form, HttpServletResponse response) {
        String id = form.getId();       
        String password = form.getPassword();

        Optional<Member> optionalMember = memberRepository.findById(id);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            System.out.println(securityConfig.encoder().encode(password) + " " + member.getPassword());
            if(securityConfig.encoder().matches(password, member.getPassword())) {
                Cookie cookie = cookieUtil.createCookie(member.getId());
                response.addCookie(cookie);
                return ResponseEntity.status(200).body("로그인 완료");
            }
        }
        return ResponseEntity.status(401).body("존재하지 않는 회원입니다.");
    }

    public ResponseEntity<?> cookieCheck(String id, HttpServletRequest request, HttpServletResponse response) {
        cookieUtil.deleteCookie(request, response, id);
        if(request.getCookies() != null){
            return ResponseEntity.status(402).body("로그인해주세요.");
        }
        return ResponseEntity.status(402).body("쿠키 확인");
    }
}
