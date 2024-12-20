package com.pet.project.dto.prescription;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MedicineVO {
    private Long id;
    private String name;
    private String specification;
    private String manufacturer;
    private String category;
    private BigDecimal unitPrice;
    private Integer stockQuantity;
    private Integer minStock;
    private Byte status;
    private LocalDateTime createdAt;
}
