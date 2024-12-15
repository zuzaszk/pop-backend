package com.pop.backend.common;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
