package com.pet.project.dto.prescription;

import lombok.Data;

@Data
public class PrescriptionDetailDTO {
    private Long medicineId;
    private Integer quantity;
    private String usage;
}
