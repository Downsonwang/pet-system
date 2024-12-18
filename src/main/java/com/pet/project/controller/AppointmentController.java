package com.pet.project.controller;

import com.pet.project.Repository.UserRepository;
import com.pet.project.common.Result;
import com.pet.project.config.JwtUtil;
import com.pet.project.dto.AppointmentDTO;
import com.pet.project.dto.AppointmentRequestDTO;
import com.pet.project.dto.AppointmentResponseDTO;
import com.pet.project.dto.AppointmentVo;
import com.pet.project.entity.Appointment;
import com.pet.project.entity.Pet;
import com.pet.project.entity.User;
import com.pet.project.exception.BusinessException;
import com.pet.project.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/create")
    public Result<?> createAppointment(
            @RequestHeader("Authorization") String token,
            @RequestParam String petName,
            @RequestParam String species,
            @RequestParam(required = false) String breed,
            @RequestParam Integer age,
            @RequestParam String gender,
            @RequestParam(required = false) Double weight,
            @RequestParam Long doctorId,
            @RequestParam String appointmentDate,
            @RequestParam String appointmentTime,
            @RequestParam String symptoms,
            @RequestParam(required = false) MultipartFile petImage
    ) {
        try {
            token = token.replace("Bearer ", "");
            AppointmentDTO appointmentDTO = AppointmentDTO.builder()
                    .petName(petName)
                    .species(species)
                    .breed(breed)
                    .age(age)
                    .gender(gender)
                    .weight(weight)
                    .doctorId(doctorId)
                    .appointmentDate(appointmentDate)
                    .appointmentTime(appointmentTime)
                    .symptoms(symptoms)
                    .build();

            Appointment appointment = appointmentService.createAppointment(token, appointmentDTO, petImage);
            return Result.success(appointment);
        } catch (BusinessException e) {
            return Result.error(e.getMessage());
        }
    }


    @GetMapping("/user")
    public Result<?> getUserAppointments(@RequestHeader("Authorization") String token) {
        try {
            // 去掉 Bearer 前缀
            token = token.replace("Bearer ", "");

            // 从token获取用户ID
            Long userId = jwtUtil.getUserIdFromToken(token);

            // 获取该用户所有宠物的预约
            List<AppointmentVo> appointments = appointmentService.getUserAppointments(userId);

            return Result.success(appointments);
        } catch (Exception e) {
            return Result.error("获取预约记录失败：" + e.getMessage());
        }
    }


    // 医生查看自己的预约列表
    @GetMapping("/doctor")
    public Result<?> getDoctorAppointments(@RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", "");
            Long doctorId = jwtUtil.getUserIdFromToken(token);
            List<AppointmentVo> appointments = appointmentService.getDoctorAppointments(doctorId);
            return Result.success(appointments);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // 确认预约
    @PostMapping("/{id}/confirm")
    public Result<?> confirmAppointment(@PathVariable Long id,
                                        @RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", "");
            Long doctorId = jwtUtil.getUserIdFromToken(token);
            appointmentService.confirmAppointment(id, doctorId);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    // 拒绝预约
    @PostMapping("/{id}/reject")
    public Result<?> rejectAppointment(@PathVariable Long id,
                                       @RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", "");
            Long doctorId = jwtUtil.getUserIdFromToken(token);
            appointmentService.rejectAppointment(id, doctorId);
            return Result.success(null);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}