package com.lovreal_be.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;

@Setter
@Getter
@Entity
public class Member {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2") // 하이픈 포함된 UUID
    @Column(columnDefinition = "VARCHAR(36)")
    private String memberString;

    @Column
    private Provider provider;

    @Column(name = "provider_member_id")
    private String  providerMemberId;


    @Column(name = "id", nullable = true)
    private String  id;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "inviteCode")
    private String inviteCode;

    @Column(name = "parterId")
    private String partnerId;

    @Column(name = "coupleDate")
    private LocalDate coupleDate;

    @Column(name = "profileImg")
    private String profileImg;

    public Member(){}

    public Member(String nickname, String id, String password, Gender gender, Provider provider) {
        this.nickname = nickname;
        this.id = id;
        this.password = password;
        this.gender = gender;
        this.provider = provider;
    }

    public Member(String nickname, String ProviderMemberId,  Gender gender, Provider provider) {
        this.nickname = nickname;
        this.providerMemberId = ProviderMemberId;
        this.gender = gender;
        this.provider = provider;
    }

}
