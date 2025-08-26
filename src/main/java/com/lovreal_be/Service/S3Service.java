package com.lovreal_be.Service;

import com.lovreal_be.Repository.S3Repository;
import com.lovreal_be.domain.Member;
import com.lovreal_be.domain.StoryImg;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Template;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.ContentStreamProvider;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.lovreal_be.Security.AuthCookieFilter.MEMBER_ID;


@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final S3Repository s3Repository;
    private final SessionService sessionService;

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    private final  S3Template  s3Template;

    public String uploadImageAndGetUrl(String objectKey, MultipartFile file, HttpServletRequest request) {
//        String memberId = request.getSession().getAttribute(MEMBER_ID).toString();
        String memberId = sessionService.findMemberIdByRequest(request);
        System.out.println("memberId = " + memberId);
        if(memberId == null){
            return null;
        }
        try (InputStream in = file.getInputStream()) {
            ObjectMetadata meta = ObjectMetadata.builder()
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();

//            s3Client.putObject(put, RequestBody.fromInputStream(in, image.getSize()));
            s3Template.upload(bucketName, objectKey, in, meta);

            // Public URL (works if the object is readable via bucket policy / ACL or through CloudFront).
            String encodedKey = URLEncoder.encode(objectKey, StandardCharsets.UTF_8);
            String src = "https://%s.s3.%s.amazonaws.com/%s".formatted(bucketName, awsRegion, encodedKey);


//
            StoryImg storyImg = new StoryImg(src, memberId);

            s3Repository.save(storyImg);
            return src;
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to read image content.");
        }
    }
}

