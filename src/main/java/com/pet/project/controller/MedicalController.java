package com.pet.project.controller;

import com.pet.project.common.Result;
import com.pet.project.config.JwtUtil;
import com.pet.project.dto.MedicalExpenseVo;
import com.pet.project.dto.MedicalRecordVo;
import com.pet.project.exception.BusinessException;
import com.pet.project.service.MedicalRecordService;
import com.pet.project.service.MedicalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class MedicalController {

    @Autowired
    private MedicalService medicalService;



    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/records/{petId}")
    public Result<?> getPetMedicalRecords(@PathVariable Long petId,
                                          @RequestHeader("Authorization") String token) {
        try {
            // 验证token和权限
            token = token.replace("Bearer ", "");
            Long userId = jwtUtil.getUserIdFromToken(token);

            List<MedicalRecordVo> records = medicalService.getPetMedicalRecords(petId);
            return Result.success(records);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/expenses/{recordId}")
    public Result<?> getMedicalExpenses(@PathVariable Long recordId) {
        try {
            List<MedicalExpenseVo> expenses = medicalService.getMedicalExpenses(recordId);
            return Result.success(expenses);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/doctor/records")
    public Result<?> getDoctorRecords(@RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", "");
            Long doctorId = jwtUtil.getUserIdFromToken(token);
            List<MedicalRecordVo> records = medicalService.getDoctorRecords(doctorId);
            return Result.success(records);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }
}
