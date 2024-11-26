package com.pop.backend.security;

import com.pop.backend.entity.UserRole;

import lombok.AllArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CustomOAuth2User implements OAuth2User {
    private final Map<String, Object> attributes;
    private final String email;
    private final String name;    
    private final List<UserRole> userRoles;

    private String jwtToken;

    
        public CustomOAuth2User(Map<String, Object> attributes, String email, String name, List<UserRole> userRoles) {
            this.attributes = attributes;
            this.email = email;
            this.name = name;
            this.userRoles = userRoles;
        }
    
        @Override
        public Map<String, Object> getAttributes() {
            return attributes;
        }
    
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return userRoles.stream()
                    .map(u -> u.getRoles().getRoleName())
                    .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase()))
                    .collect(Collectors.toList());
        }
    
        @Override
        public String getName() {
            return name;
        }
    
        public String getEmail() {
            return email;
        }
        
        public String getJwtToken() {
            return jwtToken;
        }
    
        public void setJwtToken(String jwtToken) {
            this.jwtToken = jwtToken;
    }
}
