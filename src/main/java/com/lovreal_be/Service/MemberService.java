package com.lovreal_be.Service;

import com.lovreal_be.Config.SecurityConfig;
import com.lovreal_be.Repository.MemberRepository;
import com.lovreal_be.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final SecurityConfig securityConfig;

    public ResponseEntity<?> login(String id, String password) {

        if (memberRepository.findById(id).isEmpty()) {
            return ResponseEntity.status(401).body("아이디 또는 비밀번호가 일치하지 않습니다");
        }
        Member member = memberRepository.findById(id).get();
        if (!securityConfig.encoder().matches(password, member.getPassword())) {
            return ResponseEntity.status(401).body("아이디 또는 비밀번호가 일치하지 않습니다");
        }
        String token;
        return ResponseEntity.status(200).body("로그인되었습니다.");
    }

}
