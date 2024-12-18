package com.pet.project.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class MedicalRecordVo {
    private Long id;
    private String doctorName;
    private String ownerName;
    private String diagnosis;
    private String petName;      // 添加宠物名称
    private String petSpecies;
    private String treatment;
    private String prescription;
    private LocalDateTime visitTime;
    private LocalDateTime createdAt;
    private List<MedicalExpenseVo> expenses;
}
