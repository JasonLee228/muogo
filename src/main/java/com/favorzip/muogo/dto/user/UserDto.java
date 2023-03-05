package com.favorzip.muogo.dto.user;


import com.favorzip.muogo.domain.user.Role;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private UUID id;
    private String email;
    private String username;
    private String nickname;

    private String birthDate;
    private Role role = Role.ROLE_USER;
    private String providerType;
    private String providerId;
}
