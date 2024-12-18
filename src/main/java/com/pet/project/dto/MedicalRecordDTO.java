package com.pet.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder

@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecordDTO {
    private Long appointmentId;
    private Long petId;
    private Long doctorId;
    private String diagnosis;
    private String treatment;
    private String prescription;
    private List<MedicalExpenseDTO> expenses;
}