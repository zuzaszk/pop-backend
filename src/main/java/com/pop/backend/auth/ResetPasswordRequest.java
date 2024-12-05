package com.pop.backend.auth;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    String token;
    String newPassword;
}
