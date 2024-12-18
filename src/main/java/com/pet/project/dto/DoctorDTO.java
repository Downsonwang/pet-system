package com.pet.project.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoctorDTO {
    private Long id;
    private String name;
    private String specialization;
    private String title;
}
