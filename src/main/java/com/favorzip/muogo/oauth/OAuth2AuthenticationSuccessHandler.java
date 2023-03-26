package com.favorzip.muogo.oauth;

import com.favorzip.muogo.domain.user.User;
import com.favorzip.muogo.jwt.CreateTokenDto;
import com.favorzip.muogo.jwt.TokenService;
import com.favorzip.muogo.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenService tokenService;

    private final UserRepository userRepository;

    @Value("${custom.spoty-server.front.url}")
    private String frontUrl;

    private static final String REDIRECT_URL = "/api/token?accessToken={value}&refreshToken={value}";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        // 인증 객체에서 유저 정보 조회
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        log.info("\t\t[OAuth2User.getAttributes()] = " + oAuth2User.getAttributes());

        // DB 에서 현재 저장된 정보 조회
        Optional<User> optionalUser = userRepository.findByEmail(oAuth2User.getName());
        User user = optionalUser.get();

        // 토큰 생성
        CreateTokenDto createAccessTokenDto = CreateTokenDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .provider(user.getProviderId())
                .build();

        CreateTokenDto createRefreshTokenDto = CreateTokenDto.builder()
                .userId(user.getId())
                .build();

        String accessToken = tokenService.createAccessToken(createAccessTokenDto);
        String refreshToken = tokenService.createRefreshToken(createRefreshTokenDto); // 별도 토큰 생성으로 변경 필요

        log.info("access Token: {}", accessToken);
        log.info("refresh Token: {}", refreshToken);

        // 반환 경로 - 변경 필요
        String url = frontUrl + REDIRECT_URL;

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url)
                .buildAndExpand(accessToken, refreshToken);

        // 이렇게 반환할지
        getRedirectStrategy().sendRedirect(request, response, String.valueOf(uri));

    }

}
