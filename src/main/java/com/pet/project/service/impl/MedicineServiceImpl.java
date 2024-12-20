package com.pet.project.service.impl;

import com.pet.project.Repository.MedicineRepository;
import com.pet.project.dto.MedicalExpenseVo;
import com.pet.project.dto.MedicalRecordDTO;
import com.pet.project.dto.MedicalRecordVo;
import com.pet.project.dto.prescription.MedicineDTO;
import com.pet.project.dto.prescription.MedicineVO;
import com.pet.project.entity.Medicine;
import com.pet.project.entity.Prescription;
import com.pet.project.exception.BusinessException;
import com.pet.project.service.MedicalService;
import com.pet.project.service.nil.MedicineService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.time.LocalDateTime;

@Service
public class MedicineServiceImpl implements MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;

    @Override
    public Page<MedicineVO> getAllMedicines(Pageable pageable) {
        return medicineRepository.findAll(pageable)
                .map(this::convertToVO);
    }

    @Override
    @Transactional
    public MedicineVO addMedicine(MedicineDTO medicineDTO) {
        Medicine medicine = new Medicine();
        BeanUtils.copyProperties(medicineDTO, medicine);
        medicine.setCreatedAt(LocalDateTime.now());
        medicine = medicineRepository.save(medicine);
        return convertToVO(medicine);
    }

    private MedicineVO convertToVO(Medicine medicine) {
        MedicineVO medicineVO = new MedicineVO();
        BeanUtils.copyProperties(medicine, medicineVO);
        return medicineVO;
    }
}