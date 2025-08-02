package com.lovreal_be.Repository;

import com.lovreal_be.domain.MemberCookieSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CookieRepository extends JpaRepository<MemberCookieSession, Long> {
    Optional<MemberCookieSession> findByMemberId(String memberId);
    Optional<MemberCookieSession> findMemberIdByCookieValue(String cookieValue);

}
