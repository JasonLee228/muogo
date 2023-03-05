package com.favorzip.muogo.jwt;

import com.favorzip.muogo.domain.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateTokenDto {
    private UUID userId;
    private String email;
    private Role role;
    private String provider;
}
