package com.pet.project.controller;



import com.pet.project.dto.prescription.MedicineDTO;
import com.pet.project.dto.prescription.MedicineVO;
import com.pet.project.service.nil.MedicineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/medicines")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")

public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    // 获取药品列表
    @GetMapping("/query")
    public Map<String, Object> getAllMedicines(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MedicineVO> medicinePage = medicineService.getAllMedicines(pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", medicinePage.getContent());
        response.put("page", medicinePage.getNumber());
        response.put("size", medicinePage.getSize());
        response.put("totalElements", medicinePage.getTotalElements());
        response.put("totalPages", medicinePage.getTotalPages());

        return response;
    }


    // 新增药品
    @PostMapping("/add")
    public MedicineVO addMedicine(@RequestBody MedicineDTO medicineDTO) {
        return medicineService.addMedicine(medicineDTO);
    }
}

