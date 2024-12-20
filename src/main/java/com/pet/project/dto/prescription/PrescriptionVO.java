package com.pet.project.dto.prescription;

import com.pet.project.entity.PrescriptionStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Data
public class PrescriptionVO {
    private Long id;
    private Long medicalRecordId;
    private String doctorName;
    private String petName;
    private PrescriptionStatus status;
    private LocalDateTime createdAt;
    private String notes;
    private BigDecimal totalAmount;
    private List<PrescriptionItemVO> items;
}