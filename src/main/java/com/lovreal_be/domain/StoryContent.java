package com.lovreal_be.domain;


import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Getter
public class StoryContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String content;

    @Column
    private String memberId;

    @OneToMany(mappedBy = "storyContent" , cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<StoryImg> storyImgs = new ArrayList<>();


    @Column(nullable = false, updatable = false)
    private LocalDateTime createAt;

    public StoryContent() {}

    public StoryContent(String memberId, String content) {
        this.memberId = memberId;
        this.content = content;
    }
    public StoryContent(String memberId, String content, List<StoryImg> storyImgs) {
        this.memberId = memberId;
        this.content = content;
        this.storyImgs  = storyImgs;
    }


    @PrePersist
    public void prePersist() {
        this.createAt = LocalDateTime.now();
    }

    public void addImage(StoryImg image) {
        storyImgs.add(image);
        image.setStoryContent(this); // owning side
    }
}
