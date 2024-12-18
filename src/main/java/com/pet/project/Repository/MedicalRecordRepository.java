package com.pet.project.Repository;

import com.pet.project.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    List<MedicalRecord> findByPetId(Long petId);
    Optional<MedicalRecord> findByAppointmentId(Long appointmentId);
    List<MedicalRecord> findByPetIdOrderByVisitTimeDesc(Long petId);
    List<MedicalRecord> findByDoctorIdOrderByVisitTimeDesc(Long doctorId);

}