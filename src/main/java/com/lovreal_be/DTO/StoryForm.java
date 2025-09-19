package com.lovreal_be.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class StoryForm {
    private  List<MultipartFile>  images;
    private List<StoryImgForm> imgForms;
    private String content;
    private String memberId;

    public StoryForm(String memberId, String content,List<StoryImgForm>  imgForms) {
        this.imgForms = imgForms;
        this.content = content;
        this.memberId = memberId;
    }
}
