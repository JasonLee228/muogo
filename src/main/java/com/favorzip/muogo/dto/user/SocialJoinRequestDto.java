package com.favorzip.muogo.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialJoinRequestDto {

    private UUID id;

    private String email;

    private String nickname;

}
