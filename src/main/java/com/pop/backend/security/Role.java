package com.pop.backend.security;

public enum Role {
    STUDENT("ROLE_STUDENT"),
    SUPERVISOR("ROLE_SUPERVISOR"),
    REVIEWER("ROLE_REVIEWER"),
    CHAIR("ROLE_CHAIR"),
    SPECTATOR("ROLE_SPECTATOR");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}