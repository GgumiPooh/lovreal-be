package com.lovreal_be.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class NaverProfileClient {
    public static final String NAVER_PROFILE_URL = "https://openapi.naver.com/v1/nid/me";

    public record NaverProfileResponse(
            String resultcode,
            String message,
            Response response
    ) {
        public record Response(
                String id,
                String email,
                String name,
                String nickname,
                String profile_image,
                String gender,
                String age,
                String birthday,
                String birthyear,
                String mobile
        ) {}
    }

    private final WebClient webClient;

    public NaverProfileClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl(NAVER_PROFILE_URL).build();
    }

    public NaverProfileResponse getProfile(String accessToken) {
        return webClient.get()
                .headers(h -> {
                    h.setBearerAuth(accessToken);
                    h.setAccept(List.of(MediaType.APPLICATION_JSON));
                })
                .retrieve()
                .bodyToMono(NaverProfileResponse.class).block();
    }
}

