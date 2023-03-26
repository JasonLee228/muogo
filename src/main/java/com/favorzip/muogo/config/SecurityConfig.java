package com.favorzip.muogo.config;

import com.favorzip.muogo.filter.JwtAuthenticationFilter;
import com.favorzip.muogo.jwt.TokenService;
import com.favorzip.muogo.oauth.OAuth2AuthenticationSuccessHandler;
import com.favorzip.muogo.utils.authentication.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final CorsConfig corsConfig;

    private final TokenService tokenService;

    private final CustomUserDetailService userDetailService;

    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    // request whiteList
    // 요청에 대해서 해당 경로는 인증검사를 진행하지 않는다.
    private static final String[] AUTH_WHITELIST = {
//            "/**",
            "/user/**",
            "/login/**",
            "/token/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        // SecurityConfig 설명
        //https://velog.io/@persestitan/Spring-Security-3.0.0%EB%B2%84%EC%A0%84%EC%97%90%EC%84%9C%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8B%B0-%EC%84%A4%EC%A0%95%ED%95%98%EA%B8%B0

        httpSecurity
                .addFilter(corsConfig.corsFilter())

                // 필터를 통해 유저의 토큰을 검사한다.
                .addFilterBefore(new JwtAuthenticationFilter(tokenService, userDetailService),
                        UsernamePasswordAuthenticationFilter.class)

                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .httpBasic().disable()
                .formLogin().disable()

                // 모든 요청에 대해서 검사 진행
                .authorizeHttpRequests(authorize -> authorize
                        .shouldFilterAllDispatcherTypes(false)

                        // whiteList 에 대해 전체 허용
                        .requestMatchers(AUTH_WHITELIST)
                        .permitAll()

                        // 회원 가입 url path
                        .requestMatchers("/join/**")
                        .hasAuthority("UNVERIFIED_USER")

                        // 이외 url 에 대해서 인증 처리(인증 없으면 403)
                        .anyRequest()
                        .authenticated())

                .oauth2Login().permitAll()
                .userInfoEndpoint()
                    .userService(userDetailService) // 사용자의 정보 조회 endPoint
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler); // 정보 조회 성공 시 이동할 handler, 토큰 생성 후 프론트로 반환
        // failureHandler 추가 필요

        return httpSecurity.build();

    }
}
