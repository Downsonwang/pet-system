package com.pet.project.service.impl;

import com.pet.project.Repository.DoctorRepository;
import com.pet.project.Repository.UserRepository;
import com.pet.project.dto.DoctorVo;
import com.pet.project.entity.Doctor;
import com.pet.project.entity.User;
import com.pet.project.exception.BusinessException;
import com.pet.project.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<DoctorVo> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        List<DoctorVo> doctorVos = new ArrayList<>();

        for (Doctor doctor : doctors) {
            User user = userRepository.findById(doctor.getUserId())
                    .orElseThrow(() -> new BusinessException("医生用户信息不存在"));

            DoctorVo doctorVo = DoctorVo.builder()
                    .userId(doctor.getUserId())
                    .title(doctor.getTitle())
                    .specialization(doctor.getSpecialization())
                    .introduction(doctor.getIntroduction())
                    .avatarUrl(user.getAvatarUrl())
                    .build();

            doctorVos.add(doctorVo);
        }

        return doctorVos;
    }
}