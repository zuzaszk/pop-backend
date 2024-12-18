package com.pop.backend.auth;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;

import com.pop.backend.entity.UserRole;
import com.pop.backend.security.SimpleGrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomUserDetails {
    private final Integer userId;
    private final String email;
    private final Integer role;
    private final List<UserRole> userRoles;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userRoles.stream()
                .map(u -> u.getRoles().getRoleName())
                .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase()))
                .collect(Collectors.toList());
    }
}
