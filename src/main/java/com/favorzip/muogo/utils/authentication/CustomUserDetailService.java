package com.favorzip.muogo.utils.authentication;

import com.favorzip.muogo.domain.user.Role;
import com.favorzip.muogo.domain.user.User;
import com.favorzip.muogo.exception.user.NotFoundUser;
import com.favorzip.muogo.oauth.provider.KakaoUserInfo;
import com.favorzip.muogo.oauth.provider.NaverUserInfo;
import com.favorzip.muogo.oauth.provider.OAuth2UserInfo;
import com.favorzip.muogo.repository.UserRepository;
import com.favorzip.muogo.utils.OptionalUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class CustomUserDetailService extends DefaultOAuth2UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final OptionalUtil optionalUtil;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        optionalUtil.ifEmptyThrowError(optionalUser, new NotFoundUser());

        User user = optionalUser.get();
        return new CustomUserDetails(user.getId(), user.getEmail(), user.getRole().toString());
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oauth2User = super.loadUser(userRequest);

        System.out.println("oauth2User.getAttributes() : " + oauth2User.getAttributes());

        String provider = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo = of(oauth2User, provider, userRequest.getAccessToken().getTokenValue());

        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String userName = oAuth2UserInfo.getName();
        Role role;
        UUID id = UUID.randomUUID();

        Optional<User> user = userRepository.findByEmailAndProviderType(oAuth2UserInfo.getEmail(), provider);

        // 신규 회원가입
        if(user.isEmpty()) {

            role = Role.ROLE_UNFINISHED_USER;

            User entity = User.builder()
                    .id(id)
                    .username(userName)
                    .email(email)
                    .role(role)
                    .birthDate(oAuth2UserInfo.getBirthDate())
                    .providerType(provider)
                    .providerId(providerId)
                    .build();

            userRepository.save(entity);

        } else {

            role = user.get().getRole();

        }

        // authentication 객체에 넣어준다.
        return new CustomUserDetails(id, email, role.toString(), oauth2User.getAttributes());
    }

    private OAuth2UserInfo of(OAuth2User oauth2User, String provider, String accessToken) {

        OAuth2UserInfo result = null;

        log.info("this user provider is {}", provider);

        if (provider.equals("naver")) {

            result = new NaverUserInfo(oauth2User.getAttribute("response"));

        } else if (provider.equals("kakao")) {

            result = new KakaoUserInfo(oauth2User.getAttributes());

        }
        return result;
    }
}
