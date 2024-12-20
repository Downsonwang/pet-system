package com.pet.project.Repository;

import com.pet.project.entity.Medicine;
import com.pet.project.entity.Prescription;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicineRepository  extends JpaRepository<Medicine, Long> {

    List<Medicine> findByStatus(Boolean status);

    @Query("SELECT m FROM Medicine m WHERE m.stockQuantity < m.minStock")
    List<Medicine> findLowStockMedicines();

    @Modifying
    @Query("UPDATE Medicine m SET m.stockQuantity = m.stockQuantity - :quantity WHERE m.id = :id")
    int decreaseStock(@Param("id") Long id, @Param("quantity") Integer quantity);
}
