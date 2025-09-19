package com.lovreal_be.Service;

import com.lovreal_be.repository.CookieRepository;
import com.lovreal_be.repository.MemberRepository;
import com.lovreal_be.Security.CookieUtil;
import com.lovreal_be.domain.MemberCookieSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.lovreal_be.Security.AuthCookieFilter.MEMBER_ID;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final CookieRepository cookieRepository;
    private final MemberRepository memberRepository;

    public void createCookie(HttpServletResponse response, String id) {
        String cookieValue = CookieUtil.createCookie(response);
        MemberCookieSession memberCookieSession = new MemberCookieSession(cookieValue, id);
        cookieRepository.save(memberCookieSession);
    }


    public String findMemberIdByRequest(HttpServletRequest request) {
        if(request.getSession().getAttribute(MEMBER_ID) == null) {
            return null;
        }
        return request.getSession().getAttribute(MEMBER_ID).toString();

    }
}
