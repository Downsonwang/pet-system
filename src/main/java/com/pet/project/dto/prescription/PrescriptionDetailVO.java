package com.pet.project.dto.prescription;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PrescriptionDetailVO {
    private Long id;
    private String medicineName;
    private Integer quantity;
    private String usage;
    private BigDecimal price;
}
