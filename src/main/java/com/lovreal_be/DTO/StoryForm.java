package com.lovreal_be.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class StoryForm {
    private MultipartFile[] images;
    private String content;
}
