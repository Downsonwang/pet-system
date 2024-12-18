package com.pet.project.controller;

import com.pet.project.common.Result;
import com.pet.project.config.JwtUtil;
import com.pet.project.dto.MedicalRecordDTO;
import com.pet.project.dto.MedicalRecordVo;
import com.pet.project.exception.BusinessException;
import com.pet.project.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical/record")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Autowired
    private JwtUtil jwtUtil;
    @PostMapping("/create")
    public Result<?> createRecord(@RequestBody MedicalRecordDTO recordDTO,
                                  @RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", "");
            Long doctorId = jwtUtil.getUserIdFromToken(token);
            medicalRecordService.createMedicalRecord(doctorId, recordDTO);
            return Result.success("创建成功");  // 确保返回有效的JSON
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();  // 添加日志
            return Result.error("系统错误：" + e.getMessage());
        }
    }

    @GetMapping("/history/{petId}")
    public Result<?> getMedicalHistory(@PathVariable Long petId) {
        try {
            List<MedicalRecordVo> records = medicalRecordService.getMedicalHistory(petId);
            return Result.success(records);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }


}