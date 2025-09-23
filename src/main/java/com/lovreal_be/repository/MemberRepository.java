package com.lovreal_be.repository;

import com.lovreal_be.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {
    Member findByInviteCode(String inviteCode);
    Member findByProviderMemberId(String providerMemberId);
    Member findByMemberString(String memberString);
}
