package com.lovreal_be.Repository;

import com.lovreal_be.domain.MemberCookieSession;
import com.lovreal_be.domain.StoryImg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface S3Repository extends JpaRepository<StoryImg, String> {
}
