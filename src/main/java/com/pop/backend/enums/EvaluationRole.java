package com.pop.backend.enums;

public enum EvaluationRole {
    ASSIGNED_TO_EVALUATE(1, "AssignedToEvaluate"),
    SUPERVISOR(2, "SupervisorForTheProject"),
    GENERAL_TEACHING_MEMBER(3, "GeneralTeachingMember"),
    STUDENT(4, "Student"),
    SPECTATOR(5, "Spectator");

    private final int id;
    private final String description;

    EvaluationRole(int id, String description) {
        this.id = id;
        this.description = description;
    }

    public static String getRoleNameById(int id) {
        for (EvaluationRole role : values()) {
            if (role.id == id) {
                return role.description;
            }
        }
        return "Unknown";
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public static EvaluationRole fromId(int id) {
        for (EvaluationRole role : values()) {
            if (role.id == id) {
                return role;
            }
        }
        throw new IllegalArgumentException("No EvaluationRole with id " + id);
    }
}