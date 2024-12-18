package com.pet.project.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MedicalExpenseDTO {
    private String itemName;
    private String itemType;
    private BigDecimal amount;
    private Integer quantity;
}