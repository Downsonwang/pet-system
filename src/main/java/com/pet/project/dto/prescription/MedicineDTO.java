package com.pet.project.dto.prescription;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MedicineDTO {
    private String name;
    private String specification;
    private String manufacturer;
    private String category;
    private BigDecimal unitPrice;
    private Integer stockQuantity;
    private Integer minStock;
    private Boolean status;
}

