package com.pet.project.service;

import com.pet.project.dto.MedicalRecordDTO;
import com.pet.project.dto.MedicalRecordVo;

import java.util.List;

public interface MedicalRecordService {
    void createMedicalRecord(Long doctorId, MedicalRecordDTO recordDTO);
    List<MedicalRecordVo> getMedicalHistory(Long petId);
}