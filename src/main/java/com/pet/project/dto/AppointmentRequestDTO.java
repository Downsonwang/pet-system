package com.pet.project.dto;

import com.pet.project.entity.Pet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// AppointmentRequestDTO.java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder  // 添加这个注解来启用 builder 模式

public class AppointmentRequestDTO {
    // 宠物信息
    private String petName;
    private String species;
    private String breed;
    private Integer age;
    private BigDecimal weight;
    private String avatarBase64;  // Base64编码的图片数据

    // 预约信息
    private Long doctorId;
    private String appointmentDate;
    private String appointmentTime;
    private String symptoms;
}
