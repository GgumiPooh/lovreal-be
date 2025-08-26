package com.lovreal_be.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class StoryImg {

    @Id
    @Column
    private String src;

    @Column
    private String memberId;


    public  StoryImg(String src, String memberId) {
        this.src = src;
        this.memberId = memberId;
    }

    public StoryImg() {

    }
}
