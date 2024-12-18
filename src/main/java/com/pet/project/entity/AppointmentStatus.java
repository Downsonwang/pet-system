package com.pet.project.entity;

public enum AppointmentStatus {
    PENDING("待确认"),    // 预约待确认
    CONFIRMED("已确认"),  // 医生已确认预约
    COMPLETED("已完成"),  // 就诊已完成
    CANCELLED("已取消"),  // 预约已取消
    NO_SHOW("爽约");     // 未按时就诊

    private final String description;

    AppointmentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
