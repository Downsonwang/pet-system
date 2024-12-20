package com.pet.project.dto.prescription;

import lombok.Data;

import java.util.List;

@Data
public class CreatePrescriptionRequest {
    private Long medicalRecordId;
    private String diagnosis;
    private String notes;
    private List<PrescriptionItemRequest> items;
}
