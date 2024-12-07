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
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        Users user;

        Optional<Users> existingUser = userService.findByEmailWithRole(email);
        // Optional<Users> existingUser = userService.findByEmail(email);
        Integer currentRole;

        if (existingUser.isEmpty()) {
            user = new Users();
            user.setEmail(email);
            user.setName(lastName + " " + firstName);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            user.setLastLoginAt(new Timestamp(System.currentTimeMillis()));

            try {
                userService.registerUser(user);
            } catch (Exception e) {
                e.printStackTrace();
                Integer maxUserId = userService.findMaxUserId();
                user.setUserId(maxUserId + 1);
                userService.registerUser(user);
            } finally {
                UserRole userRole = new UserRole();
                userRole.setUserId(user.getUserId());
                userRole.setRoleId(5);
                userService.insertUserRole(userRole);
                List<UserRole> roles = userService.findUserRoles(user.getUserId());
                System.out.println("User roles: " + roles);
                user.setUserRole(roles);
                // userService.setCurrentRoleForUser(user.getUserId(), 5);
                currentRole = 5;
                System.out.println("User: " + userService.getCurrentRoleForUser(user.getUserId()));
                userService.updateUser(user);
            }
        } else {
            user = existingUser.get();
            user.setLastLoginAt(new Timestamp(System.currentTimeMillis()));
            // userService.setCurrentRoleForUser(user.getUserId(), 5);
            // System.out.println("User: " + userService.getCurrentRoleForUser(user.getUserId()));
            
            // List<UserRole> roles = userService.findUserRoles(user.getUserId());
            // System.out.println("User roles: " + roles);
            // System.out.println("User role: " + user.getUserRole().get(0).getRoleId());
            currentRole = user.getUserRole().get(0).getRoleId();
            System.out.println("User role: " + currentRole);
            // user.setUserRole(roles);
            // System.out.println("User: " + user);
            // System.out.println("User roles: " + roles.get(0).getRoleId());
            userService.setCurrentRoleForUser(user.getUserId(), user.getUserRole().get(0).getRoleId());

            userService.updateUser(user);
        }

        existingUser = userService.findByEmailWithRole(email);

        String jwtToken = tokenService.generateToken(user, currentRole);
        System.out.println("Generated JWT Token: " + jwtToken);
        // return new CustomOAuth2User(oAuth2User, jwtToken);
        // return oAuth2User;

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

// @Data
// class CustomOAuth2User implements OAuth2User {
//     private final OAuth2User oAuth2User;
//     private final String jwtToken;

//     public CustomOAuth2User(OAuth2User oAuth2User, String jwtToken) {
//         this.oAuth2User = oAuth2User;
//         this.jwtToken = jwtToken;
//     }

//     public String getJwtToken() {
//         return jwtToken;
//     }
    
//     @Override
//     public Map<String, Object> getAttributes() {
//         return oAuth2User.getAttributes();
//    }

//     @Override
//     public Collection<? extends GrantedAuthority> getAuthorities() {
//         return oAuth2User.getAuthorities();
//     }

//     @Override
//     public String getName() {
//         return oAuth2User.getName();
//     }
// }
