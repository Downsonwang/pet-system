package com.pet.project.Repository;

import com.pet.project.entity.MedicalExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalExpenseRepository extends JpaRepository<MedicalExpense, Long> {
    List<MedicalExpense> findByRecordId(Long recordId);
}
