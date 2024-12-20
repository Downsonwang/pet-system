package com.pet.project.service.nil;

import com.pet.project.dto.prescription.MedicineDTO;
import com.pet.project.dto.prescription.MedicineVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MedicineService {
    Page<MedicineVO> getAllMedicines(Pageable pageable);

    MedicineVO addMedicine(MedicineDTO medicineDTO);
}
