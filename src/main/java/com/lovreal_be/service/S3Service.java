package com.lovreal_be.service;

import com.lovreal_be.repository.StoryRepository;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final StoryRepository storyRepository;
    private final SessionService sessionService;

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    private final  S3Template  s3Template;

    public List<String> uploadImageAndGetUrl(List<MultipartFile> files) {
        System.out.println("uploadImageAndGetUrl");
        List<String> urls = new ArrayList<>();
        for(MultipartFile file : files) {
            try (InputStream in = file.getInputStream()) {
                ObjectMetadata meta = ObjectMetadata.builder()
                        .contentType(file.getContentType())
                        .contentLength(file.getSize())
                        .build();

//            s3Client.putObject(put, RequestBody.fromInputStream(in, image.getSize()));
                String objectKey = "images/" + UUID.randomUUID() + "-storyImg.png";
                s3Template.upload(bucketName, objectKey, in, meta);

                // Public URL (works if the object is readable via bucket policy / ACL or through CloudFront).
                String encodedKey = URLEncoder.encode(objectKey, StandardCharsets.UTF_8);
                String url = "https://%s.s3.%s.amazonaws.com/%s".formatted(bucketName, awsRegion, encodedKey);
                urls.add(url);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to read image content.");
            }
        }
        return urls;
    }
}

