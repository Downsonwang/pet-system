package com.pet.project.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "medical_records")
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointment_id")
    private Long appointmentId;

    @Column(name = "pet_id")
    private Long petId;

    @Column(name = "doctor_id")
    private Long doctorId;

    private String diagnosis;
    private String treatment;
    private String prescription;

    @Column(name = "visit_time")
    private LocalDateTime visitTime;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}