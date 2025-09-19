package com.lovreal_be.security;

public interface OAuth2MemberInfo {

    public String getProvider();

    public String getNickname();

    public String getGender();

    public String getBirthday();

    public String getAge();
}
