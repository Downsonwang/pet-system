package com.pet.project.service.impl;

import com.pet.project.Repository.AppointmentRepository;
import com.pet.project.Repository.DoctorRepository;
import com.pet.project.Repository.PetRepository;
import com.pet.project.Repository.UserRepository;
import com.pet.project.config.JwtUtil;
import com.pet.project.dto.AppointmentDTO;
import com.pet.project.dto.AppointmentVo;
import com.pet.project.entity.*;
import com.pet.project.exception.BusinessException;
import com.pet.project.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;  // 添加 UserRepository

    @Override
    @Transactional
    public Appointment createAppointment(String token, AppointmentDTO appointmentDTO, MultipartFile petImage) {
        // 获取用户ID
        Long userId = jwtUtil.getUserIdFromToken(token);

        // 创建或更新宠物信息
        Pet pet = createOrUpdatePet(userId, appointmentDTO, petImage);

        // 验证医生是否存在
        doctorRepository.findById(appointmentDTO.getDoctorId())
                .orElseThrow(() -> new BusinessException("医生不存在"));

        // 转换日期时间字符串为 LocalDateTime
        LocalDateTime appointmentDateTime = LocalDateTime.parse(
                appointmentDTO.getAppointmentDate() + "T" + appointmentDTO.getAppointmentTime()
        );

        // 创建预约
        Appointment appointment = new Appointment();
        appointment.setPetId(pet.getId());
        appointment.setDoctorId(appointmentDTO.getDoctorId());
        appointment.setAppointmentTime(appointmentDateTime);  // 使用 LocalDateTime
        appointment.setSymptoms(appointmentDTO.getSymptoms());
        appointment.setStatus(AppointmentStatus.PENDING);

        return appointmentRepository.save(appointment);
    }
    @Override
    public List<AppointmentVo> getUserAppointments(Long userId) {
        // 获取用户的所有宠物ID
        List<Pet> userPets = petRepository.findByOwnerId(userId);
        List<Long> petIds = userPets.stream()
                .map(Pet::getId)
                .collect(Collectors.toList());

        // 获取这些宠物的所有预约
        List<Appointment> appointments = appointmentRepository.findByPetIdInOrderByAppointmentTimeDesc(petIds);

        // 转换为VO
        return appointments.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void cancelAppointment(Long userId, Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new BusinessException("预约不存在"));

        // 验证是否是用户自己的预约
        Pet pet = petRepository.findById(appointment.getPetId())
                .orElseThrow(() -> new BusinessException("宠物不存在"));

        if (!pet.getOwnerId().equals(userId)) {
            throw new BusinessException("无权操作此预约");
        }

        // 只有待确认的预约可以取消
        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new BusinessException("当前状态的预约无法取消");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    private Pet createOrUpdatePet(Long userId, AppointmentDTO dto, MultipartFile petImage) {
        Pet pet = petRepository.findByOwnerIdAndName(userId, dto.getPetName())
                .orElse(new Pet());

        pet.setOwnerId(userId);
        pet.setName(dto.getPetName());
        pet.setSpecies(dto.getSpecies());
        pet.setBreed(dto.getBreed());
        pet.setAge(dto.getAge());
        pet.setGender(Gender.valueOf(dto.getGender()));
        if (dto.getWeight() != null) {
            pet.setWeight(new BigDecimal(dto.getWeight().toString()));
        }

        if (petImage != null) {
            try {
                pet.setAvatar(petImage.getBytes());
            } catch (IOException e) {
                throw new BusinessException("宠物头像上传失败");
            }
        }

        return petRepository.save(pet);
    }

    private AppointmentVo convertToVO(Appointment appointment) {
        Pet pet = petRepository.findById(appointment.getPetId())
                .orElseThrow(() -> new BusinessException("宠物不存在"));

        Doctor doctor = doctorRepository.findById(appointment.getDoctorId())
                .orElseThrow(() -> new BusinessException("医生不存在"));

        return AppointmentVo.builder()
                .id(appointment.getId())
                .pet(pet)
                .doctor(doctor)
                .appointmentTime(Timestamp.valueOf(appointment.getAppointmentTime()))
                .status(appointment.getStatus().name())
                .symptoms(appointment.getSymptoms())
                .createdAt(Timestamp.valueOf(appointment.getCreatedAt()))
                .build();
    }



    @Override
    public List<AppointmentVo> getDoctorAppointments(Long doctorId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorIdOrderByAppointmentTimeDesc(doctorId);
        List<AppointmentVo> voList = new ArrayList<>();

        for (Appointment appointment : appointments) {
            Pet pet = petRepository.findById(appointment.getPetId())
                    .orElseThrow(() -> new BusinessException("宠物信息不存在"));
            User owner = userRepository.findById(pet.getOwnerId())
                    .orElseThrow(() -> new BusinessException("主人信息不存在"));
            AppointmentVo vo = AppointmentVo.builder()
                    .id(appointment.getId())
                    .petName(pet.getName())
                    .ownerName(owner.getUsername())
                    .appointmentTime(Timestamp.valueOf(appointment.getAppointmentTime()))
                    .symptoms(appointment.getSymptoms())
                    .status(String.valueOf(appointment.getStatus())).build();

            voList.add(vo);
        }

        return voList;
    }

    @Override
    @Transactional
    public void confirmAppointment(Long appointmentId, Long doctorId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new BusinessException("预约不存在"));

        if (!appointment.getDoctorId().equals(doctorId)) {
            throw new BusinessException("无权操作此预约");
        }
        System.out.println(appointment.getStatus());
        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new BusinessException("预约状态不正确");
        }

        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointmentRepository.save(appointment);
    }

    @Override
    @Transactional
    public void rejectAppointment(Long appointmentId, Long doctorId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new BusinessException("预约不存在"));

        if (!appointment.getDoctorId().equals(doctorId)) {
            throw new BusinessException("无权操作此预约");
        }

        if (appointment.getStatus() != AppointmentStatus.PENDING) {
            throw new BusinessException("预约状态不正确");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }
}