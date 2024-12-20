package com.pet.project.dto.prescription;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PrescriptionItemVO {
    private Long medicineId;
    private String medicineName;
    private String specification;
    private Integer quantity;
    private String usage;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}