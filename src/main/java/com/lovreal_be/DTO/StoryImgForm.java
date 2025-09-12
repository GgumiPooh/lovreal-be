package com.lovreal_be.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class StoryImgForm {
    private Long imgId;
    private String src;


    public StoryImgForm(Long imgId, String src) {
        this.src = src;
        this.imgId = imgId;
    }
}
