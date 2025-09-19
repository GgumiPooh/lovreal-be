package com.lovreal_be.service;

import com.lovreal_be.domain.Member;
import com.lovreal_be.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@Slf4j
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("아이디를 찾을 수 없습니다: " + id));

        // Spring Security의 UserDetails를 구현하는 클래스를 반환
        // 여기서는 임시로 Spring Security의 기본 User 클래스를 사용합니다.
        // 실제 애플리케이션에서는 커스텀 UserDetails 구현체를 사용하는 것이 좋습니다.
        return new User(member.getId(), member.getPassword(), Collections.emptyList() // 권한 목록 (임시로 비워둠)
        );
    }
}