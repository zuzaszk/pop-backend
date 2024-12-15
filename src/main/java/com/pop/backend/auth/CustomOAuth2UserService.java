package com.pop.backend.auth;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Optional;

import com.pop.backend.security.CustomOAuth2User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.pop.backend.entity.Users;
import com.pop.backend.service.IUsersService;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private final IUsersService userService;

    @Autowired
    private final TokenService tokenService;

    public CustomOAuth2UserService(IUsersService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        
        String email = oAuth2User.getAttribute("email");

        if (email == null) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        Users user;

        Optional<Users> existingUser = userService.findByEmailWithRole(email);

        Integer currentRole;

        if (existingUser.isEmpty()) {
            user = userService.createOAuth2User(oAuth2User);
            userService.registerUser(user);

            userService.assignRoleToUser(user.getUserId(), 5, null, null);
            if (email.endsWith("student.pwr.edu.pl")) {
                userService.assignRoleToUser(user.getUserId(), 1, null, null);
            }
            user.setUserRole(userService.findUserRoles(user.getUserId()));

        } else {
            user = existingUser.get();

            user.setLastLoginAt(new Timestamp(System.currentTimeMillis()));

            userService.updateUser(user);
        }

        existingUser = userService.findByEmailWithRole(email);
        currentRole = user.getUserRole().get(0).getRoleId();

        String jwtToken = tokenService.generateToken(user, currentRole);

        // Process the attributes and map to a custom user entity
        CustomOAuth2User customUser = processOAuth2User(registrationId, attributes, existingUser.orElse(null), jwtToken);
        
        customUser.setJwtToken(jwtToken);

        return customUser;
    }

    private CustomOAuth2User processOAuth2User(String registrationId,
                                               Map<String, Object> attributes,
                                               Users user,
                                               String jwtToken) {
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        return new CustomOAuth2User(attributes, email, name, user.getUserRole(), jwtToken);
    }

}