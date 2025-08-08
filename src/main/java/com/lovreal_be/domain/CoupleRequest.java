package com.lovreal_be.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CoupleRequest {
    @Id @Column(name = "inviteCode")
    String inviteCode;

    @ManyToOne
    private Member member;

    public CoupleRequest() {}

    public CoupleRequest(String inviteCode, Member memebr) {
        this.inviteCode = inviteCode;
        this.member = memebr;
    }
}
