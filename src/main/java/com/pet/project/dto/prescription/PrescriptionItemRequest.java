package com.pet.project.dto.prescription;

import lombok.Data;

@Data
public class PrescriptionItemRequest {
    private Long medicineId;
    private Integer quantity;
    private String usage;
}
