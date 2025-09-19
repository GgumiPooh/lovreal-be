package com.lovreal_be.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemberForm {

    private String nickname;
    private String id;
    private String password;
    private String passwordCheck;

    private String gender;
    private String coupleId;
}
