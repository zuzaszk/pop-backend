package com.pop.backend.service;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {


    public CustomOAuth2UserService() {
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        return null;
    }
    
}
