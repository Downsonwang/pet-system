package com.pet.project.controller;

import com.pet.project.dto.prescription.CreatePrescriptionRequest;
import com.pet.project.dto.prescription.PaymentRequest;
import com.pet.project.dto.prescription.PrescriptionVO;
import com.pet.project.exception.BusinessException;
import com.pet.project.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/api/prescriptions")
public class PrescriptionController {
    @Autowired
    private PrescriptionService prescriptionService;

    // 医生开具处方
    @PostMapping
    public ResponseEntity<?> createPrescription(@RequestBody CreatePrescriptionRequest request) {
        try {
            PrescriptionVO prescription = prescriptionService.createPrescription(request);
            return ResponseEntity.ok(prescription);
        } catch (BusinessException e) {
            return ResponseEntity.badRequest().body("");
        }
    }

    // 获取处方详情
    @GetMapping("/{id}")
    public ResponseEntity<?> getPrescription(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionById(id));
    }

    // 药房发药
    @PutMapping("/{id}/dispense")
    public ResponseEntity<?> dispensePrescription(@PathVariable Long id) {
        return ResponseEntity.ok(prescriptionService.dispensePrescription(id));
    }


}
