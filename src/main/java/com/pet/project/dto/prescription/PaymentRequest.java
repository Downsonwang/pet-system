package com.pet.project.dto.prescription;

import com.pet.project.entity.PaymentMethod;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private Long prescriptionId;
    private PaymentMethod paymentMethod;
    private BigDecimal amount;
}
