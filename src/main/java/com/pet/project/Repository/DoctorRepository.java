package com.pet.project.Repository;

import com.pet.project.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    // 根据医生的用户ID查找医生
    Optional<Doctor> findByUserId(Long userId);

    // 根据专业查找医生
    List<Doctor> findBySpecialization(String specialization);
}

