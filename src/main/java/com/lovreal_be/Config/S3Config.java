//package com.lovreal_be.Config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.util.StreamUtils;
//import software.amazon.awssdk.core.ResponseInputStream;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.GetObjectResponse;
//
//import java.io.IOException;
//import java.net.URI;
//import java.nio.charset.StandardCharsets;
//import java.util.UUID;
//
//@Configuration
//public class S3Config {
//
//    @Value("${aws.region}")
//    private String region;
//
//    @Value("${aws.s3.bucket}")
//    private String bucket;
//
//
//    @Bean
//    public S3Client s3Client() {
//        return S3Client.builder()
//                .region(Region.of(region))
//                .endpointOverride(URI.create(String.format("https://s3.%s.amazonaws.com",region)))
//                .forcePathStyle(true)
//                .build();
//    }
//
//}
//
//
//
