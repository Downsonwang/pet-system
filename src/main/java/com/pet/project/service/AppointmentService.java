package com.pet.project.service;

import com.pet.project.dto.AppointmentDTO;
import com.pet.project.dto.AppointmentVo;
import com.pet.project.entity.Appointment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AppointmentService {
    /**
     * 创建预约
     */
    Appointment createAppointment(String token, AppointmentDTO appointmentDTO, MultipartFile petImage);

    /**
     * 获取用户的所有预约
     */
    List<AppointmentVo> getUserAppointments(Long userId);

    /**
     * 取消预约
     */
    void cancelAppointment(Long userId, Long appointmentId);


    List<AppointmentVo> getDoctorAppointments(Long doctorId);
    void confirmAppointment(Long appointmentId, Long doctorId);
    void rejectAppointment(Long appointmentId, Long doctorId);
}
