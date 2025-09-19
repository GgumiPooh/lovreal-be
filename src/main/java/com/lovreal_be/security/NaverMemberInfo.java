package com.lovreal_be.security;

import java.util.Map;

public class NaverMemberInfo implements OAuth2MemberInfo{
    public NaverMemberInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    private Map<String, Object> attributes;

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getNickname() {
        return (String) attributes.get("name");
    }

    @Override
    public String getGender() {
        return (String) attributes.get("gender");
    }

    @Override
    public String getBirthday() {
        return (String) attributes.get("birthday");
    }

    @Override
    public String getAge() {
        return (String) attributes.get("age");
    }
}