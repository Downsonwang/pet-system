package com.pet.project.dto;

import com.pet.project.entity.AppointmentStatus;
import com.pet.project.entity.Doctor;
import com.pet.project.entity.Pet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentVo {
    private Long id;
    private Pet pet;
    private Doctor doctor;
    private String petName;
    private String ownerName;

    private Timestamp appointmentTime;
    private String status;
    private String symptoms;
    private Timestamp createdAt;
}