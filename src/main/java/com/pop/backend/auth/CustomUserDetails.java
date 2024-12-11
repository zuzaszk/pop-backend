package com.pop.backend.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomUserDetails {
    private final Integer userId;
    private final String email;
    private final Integer role;
}
