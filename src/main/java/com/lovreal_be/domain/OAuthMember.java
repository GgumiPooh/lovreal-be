package com.lovreal_be.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;

@Setter
@Getter
@Entity
public class OAuthMember {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2") // 하이픈 포함된 UUID
    @Column(columnDefinition = "VARCHAR(36)")
    private String memberString;

    @Column(name = "type")
    private Provider provider;

    @Column(name = "provider_member_id")
    private String  providerMemberId;

    @Column(name = "nickname")
    private String nickname;


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

    public OAuthMember(){}

    public OAuthMember(String providerMemberId, String nickname, Gender gender) {
        this.provider = Provider.NAVER;
        this.providerMemberId = providerMemberId;
        this.gender = gender;
        this.nickname = nickname;

    }


}
