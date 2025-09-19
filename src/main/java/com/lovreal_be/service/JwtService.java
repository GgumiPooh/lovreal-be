package com.lovreal_be.service;

import com.lovreal_be.DTO.MemberForm;
import com.lovreal_be.security.BearerTokenResolver;
import com.lovreal_be.domain.Member;
import com.lovreal_be.security.JwtTokenProvider;
import com.lovreal_be.config.SecurityConfig;
import com.lovreal_be.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
//@Transactional(readOnly = true)
public class JwtService {


    private final BearerTokenResolver bearerTokenResolver;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final SecurityConfig securityConfig;

    private static <T> T requireNonNull(T value, String name) {
        if (value == null) throw new IllegalArgumentException(name + "를 입려해주세요.");
        return value;
    }

    public String login(MemberForm loginRequest) {
        requireNonNull(loginRequest.getId(), "아이디");
        requireNonNull(loginRequest.getPassword(), "비밀번호");

        Member stored = memberRepository.findById(loginRequest.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials."));

        if (!securityConfig.encoder()
                .matches(loginRequest.getPassword(), stored.getPassword())) {
            throw new BadCredentialsException("Invalid credentials.");
        }

        return jwtTokenProvider.generateToken(stored.getId());
    }

    public Member   getTokenAndFindMember(HttpServletRequest request) {
        String token = bearerTokenResolver.resolve(request);
        System.out.println("token: " + token);
            String memberId = jwtTokenProvider.getMemberIdFromJWT(token);
            return memberRepository.findById(memberId)
                    .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials."));
        }

}


