package com.pet.project.Repository;

import com.pet.project.entity.Prescription;
import com.pet.project.entity.PrescriptionStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByStatus(PrescriptionStatus status);
    List<Prescription> findByMedicalRecord_DoctorId(Long doctorId);
    List<Prescription> findByMedicalRecord_PetId(Long petId);

    @Query("SELECT p FROM Prescription p WHERE p.status = :status AND p.createdAt >= :startDate")
    List<Prescription> findRecentPrescriptions(@Param("status") PrescriptionStatus status,
                                               @Param("startDate") LocalDateTime startDate);
}