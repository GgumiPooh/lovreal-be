package com.lovreal_be.controller;

import com.lovreal_be.service.S3Service;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/public")
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/storyImgUpload")
    public void imageUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String objectKey = "images/" + UUID.randomUUID() + "-storyImg.png";
//        s3Service.uploadImageAndGetUrl(objectKey, file, request);
    }
}
