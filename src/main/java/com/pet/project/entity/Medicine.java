package com.pet.project.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "medicines")
@Data
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String specification;
    private String manufacturer;
    private String category;
    private BigDecimal unitPrice;
    private Integer stockQuantity;
    private Integer minStock;
    private Boolean status;
}

