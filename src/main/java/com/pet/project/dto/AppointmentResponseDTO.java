package com.pet.project.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AppointmentResponseDTO {
    private Long id;
    private PetDTO pet;
    private DoctorDTO doctor;
    private LocalDateTime appointmentTime;
    private String status;
    private String symptoms;
    private LocalDateTime createdAt;
}