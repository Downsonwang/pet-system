package com.pet.project.entity;



import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "pets")
@Data
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "owner_id")
    private Long ownerId;

    private String name;
    private String species;
    private String breed;
    private Integer age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(precision = 5, scale = 2)
    private BigDecimal weight;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] avatar;

    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;
}

