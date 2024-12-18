package com.pet.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
    private String petName;
    private String species;
    private String breed;
    private Integer age;
    private String gender; // "MALE" 或 "FEMALE"
    private Double weight;
    private Long doctorId;
    private String appointmentDate;
    private String appointmentTime;
    private String symptoms;
}
