package com.pet.project.entity;

public enum ConsultationStatus {
    PENDING("待回复"),     // 病人发起咨询等待医生回复
    ANSWERED("已回复"),    // 医生已回复
    CLOSED("已关闭"),      // 咨询已结束
    CANCELLED("已取消");   // 咨询已取消

    private final String description;

    ConsultationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}