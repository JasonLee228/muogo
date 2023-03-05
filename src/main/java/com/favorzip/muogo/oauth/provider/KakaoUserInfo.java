package com.favorzip.muogo.oauth.provider;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo{

    private Map<String, Object> attributes;
    private Map<String, Object> kakao_account;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.kakao_account = (Map<String, Object>) attributes.get("kakao_account");
    }

    @Override
    public String getProviderId() {
        Long providerId = (Long) attributes.get("id");
        return providerId.toString();
    }

    @Override
    public String getProvider() {
        return "Kakao";
    }

    @Override
    public String getEmail() {
        return (String) kakao_account.get("email");
    }

    @Override
    public String getName() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        return (String) properties.get("nickname");
    }

    @Override
    public String getBirthDate() {
        return (String) kakao_account.get("birthday");
    }

    // 추가정보 더 기입
}
