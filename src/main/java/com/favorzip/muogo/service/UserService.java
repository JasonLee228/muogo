package com.favorzip.muogo.service;

import com.favorzip.muogo.domain.user.Role;
import com.favorzip.muogo.domain.user.User;
import com.favorzip.muogo.dto.user.SocialJoinRequestDto;
import com.favorzip.muogo.exception.user.ConflictUser;
import com.favorzip.muogo.jwt.TokenService;
import com.favorzip.muogo.repository.UserRepository;
import com.favorzip.muogo.utils.OptionalUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final OptionalUtil<User> optionalUtil;
    private final TokenService tokenService;

    public void checkDuplicatedUser(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            throw new ConflictUser();
        }
    }

    // 회원가입 - OAuth2 이용
    public void oauth2Join(SocialJoinRequestDto dto) {

        // email 확인, provider, role 확인

        Optional<User> optionalUser = userRepository.findById(dto.getId());

        User entity = optionalUser.get();
        entity = User.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .role(Role.ROLE_USER)
                .username(entity.getUsername())
                .providerType(entity.getProviderType())
                .providerId(entity.getProviderId())
                .birthDate(entity.getBirthDate())
                .nickname(dto.getNickname())
                .build();

        User user = userRepository.save(entity); // 이렇게 하면 그냥 덮어씌워짐. 해결 필요
        log.info("Social join user : {}", user.getEmail());

    }

}
