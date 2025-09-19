package com.lovreal_be.repository;

import com.lovreal_be.domain.StoryContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoryRepository extends JpaRepository<StoryContent, Long> {
    List<StoryContent > findByMemberId(String memberId);
}

