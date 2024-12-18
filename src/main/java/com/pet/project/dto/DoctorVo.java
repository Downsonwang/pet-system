package com.pet.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorVo {
    private Long userId;
    private String title;
    private String specialization;
    private String introduction;
    private String avatarUrl;
}