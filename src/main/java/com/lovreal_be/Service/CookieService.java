package com.lovreal_be.Service;

import com.lovreal_be.Repository.CookieRepository;
import com.lovreal_be.Repository.MemberRepository;
import com.lovreal_be.Security.CookieUtil;
import com.lovreal_be.domain.Member;
import com.lovreal_be.domain.MemberCookieSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CookieService {

    private final CookieRepository cookieRepository;
    private final MemberRepository memberRepository;

    public void createCookie(HttpServletResponse response, String id) {
        String cookieValue = CookieUtil.createCookie(response);
        MemberCookieSession memberCookieSession = new MemberCookieSession(cookieValue, id);
        cookieRepository.save(memberCookieSession);
    }

    public String findCookieByMemberId(String id) {
        return cookieRepository.findByMemberId(id)
                .map(MemberCookieSession::getCookieValue).orElse(null);
    }

    public String findMemberIdByCookieValue(String cookieValue) {
        return cookieRepository.findMemberIdByCookieValue(cookieValue)
                .map(MemberCookieSession::getMemberId).orElse(null);
    }
    public Optional<Member> cookieCheckAndGetMember(HttpServletRequest request) {
        String memberId = request.getAttribute("memberId").toString();
        System.out.println("member   Id: " + memberId);

        if (memberId == null) {
            return Optional.empty();
        }
        else return memberRepository.findById(memberId);
    }
}
