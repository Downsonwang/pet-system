package com.pet.project.controller;

import com.pet.project.common.Result;
import com.pet.project.dto.DoctorVo;
import com.pet.project.exception.BusinessException;
import com.pet.project.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    public Result<?> getAllDoctors() {
        try {
            List<DoctorVo> doctors = doctorService.getAllDoctors();
            return Result.success(doctors);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }
}