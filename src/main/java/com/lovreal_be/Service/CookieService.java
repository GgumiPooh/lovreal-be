package com.lovreal_be.Service;

import com.lovreal_be.Repository.CookieRepository;
import com.lovreal_be.Security.CookieUtil;
import com.lovreal_be.domain.MemberCookieSession;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CookieService {

    private final CookieRepository cookieRepository;

    public void createCookie(HttpServletResponse response, String id) {
        String cookieValue = CookieUtil.createCookie(response);
        MemberCookieSession memberCookieSession = new MemberCookieSession(cookieValue, id);
        cookieRepository.save(memberCookieSession);
    }

    public String findCookieById(String id) {
        return cookieRepository.findByMemberId(id)
                .map(MemberCookieSession::getCookieValue).orElse(null);
    }
}
