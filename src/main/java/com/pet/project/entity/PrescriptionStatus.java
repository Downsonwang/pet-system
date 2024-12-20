package com.pet.project.entity;

public enum PrescriptionStatus {
    CREATED("已开具"),
    DISPENSED("已发药"),
    PAID("已付费"),
    CANCELLED("已取消");

    private final String description;

    PrescriptionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
