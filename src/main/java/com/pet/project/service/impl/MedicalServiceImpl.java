package com.pet.project.service.impl;

import com.pet.project.Repository.*;
import com.pet.project.dto.MedicalExpenseDTO;
import com.pet.project.dto.MedicalExpenseVo;
import com.pet.project.dto.MedicalRecordDTO;
import com.pet.project.dto.MedicalRecordVo;
import com.pet.project.entity.*;
import com.pet.project.exception.BusinessException;
import com.pet.project.service.MedicalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class MedicalServiceImpl implements MedicalService {

    @Autowired
    private MedicalRecordRepository recordRepository;

    @Autowired
    private MedicalExpenseRepository expenseRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    @Transactional
    public void createMedicalRecord(MedicalRecordDTO recordDTO) {
        // 创建就诊记录
        MedicalRecord record = new MedicalRecord();
        record.setAppointmentId(recordDTO.getAppointmentId());
        record.setPetId(recordDTO.getPetId());
        record.setDoctorId(recordDTO.getDoctorId());
        record.setDiagnosis(recordDTO.getDiagnosis());
        record.setTreatment(recordDTO.getTreatment());
        record.setPrescription(recordDTO.getPrescription());
        record.setVisitTime(LocalDateTime.now());

        MedicalRecord savedRecord = recordRepository.save(record);

        // 保存费用明细
        if (recordDTO.getExpenses() != null) {
            for (MedicalExpenseDTO expenseDTO : recordDTO.getExpenses()) {
                MedicalExpense expense = new MedicalExpense();
                expense.setRecordId(savedRecord.getId());
                expense.setItemName(expenseDTO.getItemName());
                expense.setItemType(expenseDTO.getItemType());
                expense.setAmount(expenseDTO.getAmount());
                expense.setQuantity(expenseDTO.getQuantity());

                expenseRepository.save(expense);
            }
        }
    }

    @Override
    public List<MedicalRecordVo> getPetMedicalRecords(Long petId) {
        List<MedicalRecord> records = recordRepository.findByPetId(petId);
        List<MedicalRecordVo> voList = new ArrayList<>();

        for (MedicalRecord record : records) {
            Doctor doctor = doctorRepository.findById(record.getDoctorId())
                    .orElseThrow(() -> new BusinessException("医生信息不存在"));

            MedicalRecordVo vo = MedicalRecordVo.builder()
                    .id(record.getId())
                    .doctorName(doctor.getTitle())
                    .diagnosis(record.getDiagnosis())
                    .treatment(record.getTreatment())
                    .prescription(record.getPrescription())
                    .visitTime(record.getVisitTime())
                    .createdAt(record.getCreatedAt())
                    .build();

            voList.add(vo);
        }

        return voList;
    }

    @Override
    public List<MedicalExpenseVo> getMedicalExpenses(Long recordId) {
        List<MedicalExpense> expenses = expenseRepository.findByRecordId(recordId);
        List<MedicalExpenseVo> voList = new ArrayList<>();

        for (MedicalExpense expense : expenses) {
            MedicalExpenseVo vo = MedicalExpenseVo.builder()
                    .id(expense.getId())
                    .itemName(expense.getItemName())
                    .itemType(expense.getItemType())
                    .amount(expense.getAmount())
                    .quantity(expense.getQuantity())
                    .total(expense.getAmount().multiply(new BigDecimal(expense.getQuantity())))
                    .build();

            voList.add(vo);
        }

        return voList;
    }

    @Override
    public List<MedicalRecordVo> getDoctorRecords(Long doctorId) {
        // 获取该医生的所有诊断记录
        List<MedicalRecord> records = recordRepository.findByDoctorIdOrderByVisitTimeDesc(doctorId);

        return records.stream().map(record -> {
            // 获取宠物信息
            Pet pet = petRepository.findById(record.getPetId())
                    .orElseThrow(() -> new BusinessException("宠物信息不存在"));

            // 获取主人信息
            User owner = userRepository.findById(pet.getOwnerId())
                    .orElseThrow(() -> new BusinessException("主人信息不存在"));

            return MedicalRecordVo.builder()
                    .id(record.getId())
                    .visitTime(record.getVisitTime())
                    .petName(pet.getName())
                    .petSpecies(pet.getSpecies())
                    .ownerName(owner.getUsername())
                    .diagnosis(record.getDiagnosis())
                    .treatment(record.getTreatment())
                    .prescription(record.getPrescription())
                    .build();
        }).collect(Collectors.toList());
    }
}