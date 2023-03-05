package com.favorzip.muogo.jwt;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RefreshToken {

    public static final int DEFAULT_TTL_DAY = 7;

    private String id;

    private String token;

    private int expiration = DEFAULT_TTL_DAY;

    @Builder
    public RefreshToken(String id, String token) {
        this.id = id;
        this.token = token;
    }
}
