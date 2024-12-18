package com.pet.project.entity;

public enum PrescriptionStatus {
    PENDING("待处理"),     // 医生刚开具的处方
    PROCESSING("处理中"),  // 药房正在处理
    DISPENSED("已发药"),   // 药已发放
    COMPLETED("已完成"),   // 病人已取药
    CANCELLED("已取消");   // 处方已取消

    private final String description;

    PrescriptionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
