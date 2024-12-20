package com.pet.project.service;

import com.pet.project.dto.MedicalExpenseVo;
import com.pet.project.dto.MedicalRecordDTO;
import com.pet.project.dto.MedicalRecordVo;

import java.util.List;

public interface MedicalService {
    // 获取宠物就诊记录
    List<MedicalRecordVo> getPetMedicalRecords(Long petId);

    List<MedicalRecordVo> getDoctorRecords(Long doctorId);

    // 获取就诊费用明细
    List<MedicalExpenseVo> getMedicalExpenses(Long recordId);

    // 创建就诊记录和费用
    void createMedicalRecord(MedicalRecordDTO recordDTO);


}
