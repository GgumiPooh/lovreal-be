package com.lovreal_be.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class StoryImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imgId;

    @Column
    private String src;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "story_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_story_id"))
    private Story story;


    public  StoryImg(String src) {
        this.src = src;
    }

    public StoryImg() {

    }
}
