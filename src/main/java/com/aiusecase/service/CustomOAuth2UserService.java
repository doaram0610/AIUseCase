package com.aiusecase.service;

import com.aiusecase.model.KakaoUserInfo;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        
        // 카카오 사용자 정보 추출
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        
        Long id = Long.valueOf(attributes.get("id").toString());
        String nickname = (String) profile.get("nickname");
        String email = (String) kakaoAccount.get("email");
        String profileImageUrl = (String) profile.get("profile_image_url");
        String thumbnailImageUrl = (String) profile.get("thumbnail_image_url");
        
        // 카카오 사용자 정보 객체 생성
        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(
            id, nickname, email, profileImageUrl, thumbnailImageUrl
        );
        
        // OAuth2User 객체에 카카오 사용자 정보 추가
        Map<String, Object> customAttributes = new HashMap<>(attributes);
        customAttributes.put("kakaoUserInfo", kakaoUserInfo);
        
        return new DefaultOAuth2User(
            oAuth2User.getAuthorities(),
            customAttributes,
            "id"
        );
    }
}
