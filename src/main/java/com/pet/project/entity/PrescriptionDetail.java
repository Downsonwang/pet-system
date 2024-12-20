package com.pet.project.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

// 新增处方明细实体
@Entity
@Table(name = "prescription_details")
@Data
public class PrescriptionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prescription_id")
    private Prescription prescription;

    @ManyToOne
    @JoinColumn(name = "medicine_id")
    private Medicine medicine;

    private Integer quantity;  // 数量
    private BigDecimal unitPrice;  // 单价
    private String usage;      // 用法用量
}
