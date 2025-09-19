package com.lovreal_be.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class Member {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "password")
    private String password;

    @Column(name = "gender")
    private String gender;

    @Column(name = "inviteCode")
    private String inviteCode;

    @Column(name = "parterId")
    private String partnerId;

    @Column(name = "coupleDate")
    private LocalDate coupleDate;

    @Column(name = "profileImg")
    private String profileImg;

    public Member(){}

    public Member(String nickname, String id, String password, String gender) {
        this.nickname = nickname;
        this.id = id;
        this.password = password;
        this.gender = gender;
    }

    public Member(String id, String gender) {
        this.id = id;
        this.gender = gender;

    }
}

