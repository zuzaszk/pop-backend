package com.pop.backend.auth;

import lombok.Data;

@Data
public class RegistrationRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String username;
    private Integer invitationId;
}
