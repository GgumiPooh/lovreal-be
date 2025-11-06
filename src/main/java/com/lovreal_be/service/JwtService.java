package com.lovreal_be.service;

import com.lovreal_be.DTO.MemberForm;
import com.lovreal_be.domain.Member;
import com.lovreal_be.security.CookieTokenResolver;
import com.lovreal_be.security.JwtTokenProvider;
import com.lovreal_be.config.SecurityConfig;
import com.lovreal_be.repository.MemberRepository;
import com.lovreal_be.security.TokenResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
//@Transactional(readOnly = true)
public class JwtService {


    private final TokenResolver tokenResolver = new CookieTokenResolver();
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final SecurityConfig securityConfig;

    private static <T> T requireNonNull(T value, String name) {
        if (value == null) throw new IllegalArgumentException(name + "를 입려해주세요.");
        return value;
    }

    public Member login(MemberForm loginRequest, HttpServletResponse response) {
        requireNonNull(loginRequest.getId(), "아이디");
        requireNonNull(loginRequest.getPassword(), "비밀번호");

        Member member = memberRepository.findById(loginRequest.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials."));

        if (!securityConfig.encoder()
                .matches(loginRequest.getPassword(), member.getPassword())) {
            throw new BadCredentialsException("Invalid credentials.");
        }

        String token = jwtTokenProvider.generateToken(member.getMemberString());
        jwtTokenProvider.addTokenToCookie(token, response);
        return member;
    }

    public Member getTokenAndFindMember(HttpServletRequest request) {
        String token = tokenResolver.resolve(request);
        System.out.println("token: " + token);
            return jwtTokenProvider.getMemberFromJWT(token);
//            return memberRepository.findById(memberString).orElseThrow(() -> new UsernameNotFoundException("Invalid credentials."));
        }

}


