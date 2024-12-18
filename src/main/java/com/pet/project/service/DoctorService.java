package com.pet.project.service;

import com.pet.project.dto.DoctorVo;
import org.springframework.context.annotation.Bean;

import java.util.List;

public interface DoctorService {
    List<DoctorVo> getAllDoctors();
}