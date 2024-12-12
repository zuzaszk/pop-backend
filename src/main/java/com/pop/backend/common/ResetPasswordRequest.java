package com.pop.backend.common;

import lombok.Data;

@Data
public class ResetPasswordRequest {
    String token;
    String newPassword;
}
