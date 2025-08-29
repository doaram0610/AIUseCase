package com.aiusecase.model;

import lombok.Data;

@Data
public class KakaoUserInfo {
    private Long id;
    private String nickname;
    private String email;
    private String profileImageUrl;
    private String thumbnailImageUrl;
    
    public KakaoUserInfo(Long id, String nickname, String email) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
    }
    
    public KakaoUserInfo(Long id, String nickname, String email, String profileImageUrl, String thumbnailImageUrl) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.thumbnailImageUrl = thumbnailImageUrl;
    }
}
