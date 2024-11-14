package com.pop.backend.oauth;

import java.sql.Timestamp;
import java.util.Optional;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.pop.backend.entity.Users;
import com.pop.backend.service.IUsersService;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {


    private final IUsersService userService;

    public CustomOAuth2UserService(IUsersService userService) {
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        
        String email = oAuth2User.getAttribute("email");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");

        Optional<Users> existingUser = userService.findByEmail(email);
        if (existingUser.isEmpty()) {
            Users newUser = new Users();
            // newUser.setUserId(10000000);
            newUser.setEmail(email);
            newUser.setName(lastName + " " + firstName);
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            newUser.setLastLoginAt(new Timestamp(System.currentTimeMillis()));

            userService.registerOAuthUser(newUser);
        }

        return oAuth2User;
    }
    
}
