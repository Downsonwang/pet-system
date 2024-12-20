package com.pet.project.dto.prescription;

import lombok.Data;

import java.util.List;

@Data
public class PrescriptionDTO {
    private Long medicalRecordId;
    private String notes;
    private List<PrescriptionDetailDTO> details;
}
