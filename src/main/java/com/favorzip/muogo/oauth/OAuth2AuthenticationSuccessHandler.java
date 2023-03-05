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
    private String spotyServerFrontUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("oAuth2User.getAttributes() = " + oAuth2User.getAttributes());

        Optional<User> optionalUser = userRepository.findByEmail(oAuth2User.getName());

        User user = optionalUser.get();

        CreateTokenDto createTokenDto = CreateTokenDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .provider(user.getProviderId())
                .build();

        String accessToken = tokenService.createAccessToken(createTokenDto);
        String refreshToken = tokenService.createAccessToken(createTokenDto);

        log.info("access Token: {}", accessToken);
        log.info("refresh Token: {}", refreshToken);

        String url = spotyServerFrontUrl + "/api/token?accessToken={value}&refreshToken={value}";

        UriComponents uri = UriComponentsBuilder.fromHttpUrl(url)
                .buildAndExpand(accessToken, refreshToken);

        System.out.println("uri = " + uri);

        // 이렇게 반환할지
        getRedirectStrategy().sendRedirect(request, response, String.valueOf(uri));

    }

}
