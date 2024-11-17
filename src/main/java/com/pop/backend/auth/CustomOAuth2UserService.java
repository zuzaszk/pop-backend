package com.pop.backend.auth;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.pop.backend.security.CustomOAuth2User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.pop.backend.entity.UserRole;
import com.pop.backend.entity.Users;
import com.pop.backend.service.IUsersService;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
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
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        Optional<Users> existingUser = userService.findByEmailWithRole(email);
        if (existingUser.isEmpty()) {
            Users newUser = new Users();
            newUser.setEmail(email);
            newUser.setName(lastName + " " + firstName);
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            newUser.setLastLoginAt(new Timestamp(System.currentTimeMillis()));

            try {
                userService.registerUser(newUser);
            } catch (Exception e) {
                e.printStackTrace();
                Integer maxUserId = userService.findMaxUserId();
                newUser.setUserId(maxUserId + 1);
                userService.registerUser(newUser);
            } finally {
                UserRole userRole = new UserRole();
                userRole.setUserId(newUser.getUserId());
                userRole.setRoleId(5);
                userService.insertUserRole(userRole);
                List<UserRole> roles = userService.findUserRoles(newUser.getUserId());
                System.out.println("User roles: " + roles);
                newUser.setUserRole(roles);
                userService.updateUser(newUser);
            }
        }

        // Process the attributes and map to a custom user entity
        CustomOAuth2User customUser = processOAuth2User(registrationId, attributes, existingUser.orElse(null));
        
        return customUser;
    }

    private CustomOAuth2User processOAuth2User(String registrationId,
                                               Map<String, Object> attributes,
                                               Users user) {
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        return new CustomOAuth2User(attributes, email, name, user.getUserRole());
    }

}
