package com.pet.project.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PetDTO {
    private Long id;
    private String name;
    private String species;
    private String breed;
    private Integer age;
    private String gender;
    private BigDecimal weight;
    private String avatarUrl;
}