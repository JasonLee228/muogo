package com.favorzip.muogo.controller;

import com.favorzip.muogo.dto.user.SocialJoinRequestDto;
import com.favorzip.muogo.jwt.TokenService;
import com.favorzip.muogo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/join")
@RestController
public class JoinController {

    private final UserService userService;

    private final TokenService tokenService;

    @PostMapping("/social")
    @ResponseBody
    public ResponseEntity<String> socialJoin(
            @RequestBody(required = false) SocialJoinRequestDto dto,
            HttpServletRequest request) {

        String token = tokenService.getToken(request);
        log.info("token = {}", token);
        userService.oauth2Join(dto);
        return ResponseEntity.ok("회원가입 완료!");
    }

}
