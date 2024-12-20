package com.pet.project.service.impl;

import com.pet.project.Repository.MedicalRecordRepository;
import com.pet.project.Repository.MedicineRepository;
import com.pet.project.Repository.PrescriptionRepository;
import com.pet.project.dto.prescription.*;
import com.pet.project.entity.*;
import com.pet.project.exception.BusinessException;
import com.pet.project.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
public class PrescriptionServiceImpl implements PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @Override
    public PrescriptionVO createPrescription(CreatePrescriptionRequest request) {
        // 1. 验证医疗记录
        MedicalRecord medicalRecord = medicalRecordRepository.findById(request.getMedicalRecordId())
                .orElseThrow(() -> new BusinessException("找不到相关就诊记录"));

        // 2. 验证所有药品的库存
        validateMedicinesStock(request.getItems());

        // 3. 创建处方主记录
        Prescription prescription = new Prescription();
        prescription.setMedicalRecord(medicalRecord);
        prescription.setNotes(request.getNotes());
        prescription.setStatus(PrescriptionStatus.CREATED);
        prescription.setCreatedAt(LocalDateTime.now());

        // 4. 创建处方明细并计算总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<PrescriptionDetail> details = new ArrayList<>();

        for (PrescriptionItemRequest item : request.getItems()) {
            Medicine medicine = medicineRepository.findById(item.getMedicineId())
                    .orElseThrow(() -> new BusinessException("药品不存在"));

            PrescriptionDetail detail = new PrescriptionDetail();
            detail.setPrescription(prescription);
            detail.setMedicine(medicine);
            detail.setQuantity(item.getQuantity());
            detail.setUsage(item.getUsage());
            detail.setUnitPrice(medicine.getUnitPrice());

            BigDecimal itemTotal = medicine.getUnitPrice()
                    .multiply(new BigDecimal(item.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            details.add(detail);
        }

        prescription.setDetails(details);
        prescription.setTotalAmount(totalAmount);

        // 5. 保存处方
        prescription = prescriptionRepository.save(prescription);

        // 6. 转换为VO并返回
        return convertToVO(prescription);
    }

    @Override
    public PrescriptionVO getPrescriptionById(Long id) {
        return null;
    }

    @Override
    public PrescriptionVO dispensePrescription(Long id) {
        return null;
    }

    @Override
    public List<PrescriptionVO> getPendingPrescriptions() {
        return List.of();
    }

    @Override
    public List<PrescriptionVO> getDoctorPrescriptions(Long doctorId) {
        return List.of();
    }

    @Override
    public List<PrescriptionVO> getPetPrescriptions(Long petId) {
        return List.of();
    }

    private void validateMedicinesStock(List<PrescriptionItemRequest> items) {
        for (PrescriptionItemRequest item : items) {
            Medicine medicine = medicineRepository.findById(item.getMedicineId())
                    .orElseThrow(() -> new BusinessException("药品不存在"));

            if (medicine.getStockQuantity() < item.getQuantity()) {
                throw new BusinessException(String.format(
                        "药品[%s]库存不足，当前库存: %d，需求数量: %d",
                        medicine.getName(),
                        medicine.getStockQuantity(),
                        item.getQuantity()
                ));
            }
        }
    }

    private PrescriptionVO convertToVO(Prescription prescription) {
        PrescriptionVO vo = new PrescriptionVO();
        vo.setId(prescription.getId());

        vo.setMedicalRecordId(prescription.getMedicalRecord().getId());
        vo.setDoctorName(String.valueOf(prescription.getMedicalRecord().getDoctorId()));
        vo.setPetName(String.valueOf(prescription.getMedicalRecord().getPetId()));
        vo.setStatus(prescription.getStatus());
        vo.setCreatedAt(prescription.getCreatedAt());
        vo.setNotes(prescription.getNotes());
        vo.setTotalAmount(prescription.getTotalAmount());

        List<PrescriptionItemVO> itemVOs = prescription.getDetails().stream()
                .map(this::convertToItemVO)
                .collect(Collectors.toList());
        vo.setItems(itemVOs);

        return vo;
    }

    private PrescriptionItemVO convertToItemVO(PrescriptionDetail detail) {
        PrescriptionItemVO vo = new PrescriptionItemVO();
        vo.setMedicineId(detail.getMedicine().getId());
        vo.setMedicineName(detail.getMedicine().getName());
        vo.setSpecification(detail.getMedicine().getSpecification());
        vo.setQuantity(detail.getQuantity());
        vo.setUsage(detail.getUsage());
        vo.setUnitPrice(detail.getUnitPrice());
        vo.setTotalPrice(detail.getUnitPrice().multiply(new BigDecimal(detail.getQuantity())));
        return vo;
    }
}
