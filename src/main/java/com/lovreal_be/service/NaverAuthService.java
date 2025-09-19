package com.lovreal_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NaverAuthService {
    @Value("${naver.client.id}")
    private String clientId;
    @Value("${naver.client.secret}")
    private String clientSecret;
    @Value("${naver.redirect-uri}")
    private String redirectUri;

    private static final String NAVER_AUTHORIZE = "https://nid.naver.com/oauth2.0/authorize";
    private static final String NAVER_TOKEN = "https://nid.naver.com/oauth2.0/token";

    public record AuthRedirect(String state, String redirectUri) {}
    public AuthRedirect createAuthorizeUrl() {
        String state = UUID.randomUUID().toString();
        String url =  UriComponentsBuilder.fromUriString(NAVER_AUTHORIZE)
                .queryParam("response_type", "code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri) // 등록된 콜백과 정확히 동일해야 함
                .queryParam("state", state)
                .encode(StandardCharsets.UTF_8) // 중요: 인코딩
                .build()
                .toUriString();
        return new AuthRedirect(state, url);
    }

    public TokenResponse exchangeToken(String code, String state) {

        RestClient client = RestClient.builder().build();

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("code", code);
        form.add("state", state);
        form.add("redirect_uri", redirectUri);

        TokenResponse tokens = client.post()
                .uri(NAVER_TOKEN)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form) // ← WebFlux 필요 없음
                .retrieve()
                .body(TokenResponse.class);
        return tokens;
    }


    public record TokenResponse(
            String access_token,
            String refresh_token,
            String token_type,
            Integer expires_in,
            String error,
            String error_description
    ) {}
}