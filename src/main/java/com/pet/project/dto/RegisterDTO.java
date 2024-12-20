package com.pet.project.dto;

import com.pet.project.entity.User;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RegisterDTO {
    private String username;
    private String password;
    private String email;
    private MultipartFile avatar;
    private String specialization;
    private String introduction;
    private String title;
    private User.UserRole role;

    // 药房工作人员特有信息
    private String department;         // 所属药房部门
    private String pharmacistLicense;  // 药剂师执照号
    private String pharmacyLocation;   // 工作地点

}





