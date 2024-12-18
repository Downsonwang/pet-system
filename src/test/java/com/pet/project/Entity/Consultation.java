package com.pet.project.Entity;

import com.pet.project.entity.ConsultationStatus;
import com.pet.project.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "consultations")
@Data
public class Consultation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private User doctor;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private Pet pet;

    private String question;
    private String answer;

    @Enumerated(EnumType.STRING)
    private ConsultationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime answeredAt;
}
