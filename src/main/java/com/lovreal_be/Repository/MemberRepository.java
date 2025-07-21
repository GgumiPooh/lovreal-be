package com.lovreal_be.Repository;

import com.lovreal_be.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


public interface MemberRepository extends JpaRepository<Member, String> {
}
