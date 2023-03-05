package com.favorzip.muogo.domain.user;

import com.favorzip.muogo.domain.BaseTimeEntity;
import com.favorzip.muogo.dto.user.SocialJoinRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;


@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(columnDefinition = "VARCHAR(100)")
    private String email;

    @Column(columnDefinition = "VARCHAR(100)")
    private String username;

    @Column(columnDefinition = "VARCHAR(100)")
    private String nickname;

    @Column(columnDefinition = "VARCHAR(100)")
    private String birthDate;

    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;

    @Column(columnDefinition = "VARCHAR(20)")
    private String providerType;

    private String providerId;

    public void setRole(Role role) {
        this.role = role;
    }

    public User(SocialJoinRequestDto dto) {
        this.id = dto.getId();
        this.email = dto.getEmail();
        this.nickname = dto.getNickname();
    }
}

