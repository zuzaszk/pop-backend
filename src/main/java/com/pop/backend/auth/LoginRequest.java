package com.pop.backend.auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
