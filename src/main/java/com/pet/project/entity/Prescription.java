package com.pet.project.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "prescriptions")
@Data
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medical_record_id")
    private MedicalRecord medicalRecord;

    @Enumerated(EnumType.STRING)
    private PrescriptionStatus status;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
}
