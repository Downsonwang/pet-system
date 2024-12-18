package com.pet.project.Repository;

import com.pet.project.entity.Appointment;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    // 检查某个医生在特定时间是否已有预约
    boolean existsByDoctorIdAndAppointmentTime(Long doctorId, LocalDateTime appointmentTime);

    // 获取某个用户的所有预约
    List<Appointment> findByPetIdInOrderByAppointmentTimeDesc(List<Long> petIds);

    List<Appointment> findByDoctorIdOrderByAppointmentTimeDesc(Long doctorId);

    // 获取某个医生某天的所有预约
    @Query("SELECT a FROM Appointment a WHERE a.doctorId = :doctorId AND DATE(a.appointmentTime) = DATE(:date)")
    List<Appointment> findByDoctorIdAndDate(@Param("doctorId") Long doctorId, @Param("date") LocalDateTime date);
}