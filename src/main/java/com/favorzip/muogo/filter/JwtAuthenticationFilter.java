package com.favorzip.muogo.filter;

import com.favorzip.muogo.exception.BaseException;
import com.favorzip.muogo.jwt.TokenService;
import com.favorzip.muogo.utils.authentication.CustomUserDetailService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 매 요청마다 JWT 가 유효한지 검증하고, 유효할 시 해당 유저에 Security Context 를 인가 해주는 필터
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String[] whitelist = {"/", "/api/join", "/api/join/email", "/api/login"}; // 동작 안함

    private final TokenService tokenService;

    private final CustomUserDetailService userDetailsService;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        String requestURI = request.getRequestURI();

        //whiteList 처리
        if (checkIsWhitelist(requestURI)) {
            log.info("is whiteList request : {}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        // 헤더가 필요한 요청에 대하여 헤더가 비어있을 때 - 시작
        if (request.getHeader("Authorization") == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // 헤더에서 JWT 를 받아옵니다.
        String token = tokenService.getToken(request);

        try {
            tokenService.validateToken(token);
        } catch (BadCredentialsException | SignatureException | BaseException | ExpiredJwtException ex) {
            filterChain.doFilter(request, response);
            return;
        }

        String email = tokenService.getEmail(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // JWT 를 바탕으로 인증 객체 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        System.out.println("authToken = " + authToken);
        // SecurityContextHolder 에 저장
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }


    /**
     * whiteList 일 경우, true 를 반환한다.
     */
    private boolean checkIsWhitelist(String requestURI) {
        return PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }

}
