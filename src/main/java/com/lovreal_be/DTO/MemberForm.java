package com.lovreal_be.DTO;

import com.lovreal_be.domain.Gender;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class MemberForm {

    private String nickname;
    private String id;
    private String password;
    private String passwordCheck;
    private Gender gender;
    private String coupleId;
}
