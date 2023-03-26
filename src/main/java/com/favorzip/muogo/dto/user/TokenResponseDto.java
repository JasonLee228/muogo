package com.favorzip.muogo.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TokenResponseDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResponseDto {
        private String accessToken;
        private String refreshToken;
    }
}
