package com.pet.project.service.impl;

import com.pet.project.Repository.AppointmentRepository;
import com.pet.project.Repository.MedicalRecordRepository;
import com.pet.project.Repository.PetRepository;
import com.pet.project.Repository.UserRepository;
import com.pet.project.dto.MedicalRecordDTO;
import com.pet.project.dto.MedicalRecordVo;
import com.pet.project.entity.*;
import com.pet.project.exception.BusinessException;
import com.pet.project.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Override
    @Transactional
    public void createMedicalRecord(Long doctorId, MedicalRecordDTO recordDTO) {
        // 获取预约信息
        Appointment appointment = appointmentRepository.findById(recordDTO.getAppointmentId())
                .orElseThrow(() -> new BusinessException("预约不存在"));

        // 验证医生权限
        if (!appointment.getDoctorId().equals(doctorId)) {
            throw new BusinessException("无权创建此就诊记录");
        }

        // 验证预约状态
        if (appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new BusinessException("预约状态不正确，无法创建就诊记录");
        }

        // 创建就诊记录
        MedicalRecord record = new MedicalRecord();
        record.setAppointmentId(recordDTO.getAppointmentId());
        record.setPetId(appointment.getPetId());
        record.setDoctorId(doctorId);
        record.setDiagnosis(recordDTO.getDiagnosis());
        record.setTreatment(recordDTO.getTreatment());
        record.setPrescription(recordDTO.getPrescription());
        record.setVisitTime(LocalDateTime.now());

        medicalRecordRepository.save(record);

        // 更新预约状态
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);
    }

    @Override
    public List<MedicalRecordVo> getMedicalHistory(Long petId) {
        List<MedicalRecord> records = medicalRecordRepository.findByPetIdOrderByVisitTimeDesc(petId);

        return records.stream().map(record -> {
            return MedicalRecordVo.builder()
                    .id(record.getId())
                    .visitTime(record.getVisitTime())
                    .diagnosis(record.getDiagnosis())
                    .treatment(record.getTreatment())
                    .prescription(record.getPrescription())
                    .build();
        }).collect(Collectors.toList());
    }




}
