package com.pet.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "appointments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pet_id")
    private Long petId;

    @Column(name = "doctor_id")
    private Long doctorId;

    @Column(name = "appointment_time")
    private LocalDateTime appointmentTime;  // 改为 LocalDateTime

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @Column(columnDefinition = "TEXT")
    private String symptoms;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;  // 改为 LocalDateTime

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

