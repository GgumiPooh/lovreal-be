package com.lovreal_be.repository;

import com.lovreal_be.domain.Member;
import com.lovreal_be.domain.OAuthMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuthMemberRepository extends JpaRepository<OAuthMember, String> {
    public OAuthMember findByProviderMemberId(String providerMemberId);
}

