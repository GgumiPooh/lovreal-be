package com.lovreal_be.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Member {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "password")
    private String password;

    @Column(name = "gender")
    private String gender;

    @Column(name = "inviteCode")
    private String inviteCode;

    public Member(String id, String password, String gender) {
        this.id = id;
        this.password = password;
        this.gender = gender;
    }

    public Member() {

    }
}

