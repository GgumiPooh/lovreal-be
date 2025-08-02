package com.lovreal_be.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class MemberCookieSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Column(name = "cookie_value", nullable = false, unique = true)
    private String cookieValue;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public MemberCookieSession() {

    }
    public MemberCookieSession(String cookieValue, String memberId) {
        this.cookieValue = cookieValue;
        this.memberId = memberId;
    }

}
