package com.aiusecase.controller;

import com.aiusecase.model.KakaoUserInfo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @GetMapping("/")
    public String home(@AuthenticationPrincipal OAuth2User oAuth2User, Model model) {
        if (oAuth2User != null) {
            KakaoUserInfo kakaoUserInfo = (KakaoUserInfo) oAuth2User.getAttributes().get("kakaoUserInfo");
            model.addAttribute("user", kakaoUserInfo);
            return "home";
        }
        return "index";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "로그인에 실패했습니다. 다시 시도해주세요.");
        }
        return "login";
    }

    @GetMapping("/home")
    public String userHome(@AuthenticationPrincipal OAuth2User oAuth2User, Model model) {
        if (oAuth2User != null) {
            KakaoUserInfo kakaoUserInfo = (KakaoUserInfo) oAuth2User.getAttributes().get("kakaoUserInfo");
            model.addAttribute("user", kakaoUserInfo);
        }
        return "home";
    }
}
