package com.lovreal_be.Repository;

import com.lovreal_be.domain.MemberCookieSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CookieRepository extends JpaRepository<MemberCookieSession, Long> {
}
