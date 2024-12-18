package com.pet.project.service.impl;


import com.pet.project.Repository.DoctorRepository;
import com.pet.project.Repository.UserRepository;
import com.pet.project.config.FileUtil;
import com.pet.project.config.JwtUtil;
import com.pet.project.dto.LoginDTO;
import com.pet.project.dto.LoginVo;
import com.pet.project.dto.RegisterDTO;
import com.pet.project.entity.Doctor;
import com.pet.project.entity.User;
import com.pet.project.entity.UserStatus;
import com.pet.project.exception.BusinessException;
import com.pet.project.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;

@Service
@Transactional(rollbackFor = Exception.class)
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DoctorRepository doctorRepository;  // 添加注入

    @Autowired
    private FileUtil fileUtil;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterDTO registerDTO) throws BusinessException {
        // 基本验证
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new BusinessException("用户名已存在");
        }

        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new BusinessException("邮箱已被使用");
        }

        // 创建用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setEmail(registerDTO.getEmail());
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setStatus(UserStatus.NORMAL.getCode());
        user.setRole(registerDTO.getRole());

        // 处理头像
        try {
            String fileName = fileUtil.saveFile(registerDTO.getAvatar());
            user.setAvatarUrl("/uploads/" + fileName);
        } catch (IOException e) {
            throw new BusinessException("头像上传失败");
        }

        // 保存用户
        userRepository.save(user);

        // 如果是医生,保存医生信息
        if (User.UserRole.DOCTOR.equals(registerDTO.getRole())) {  // 使用枚举比较
            Doctor doctor = new Doctor();
            doctor.setUserId(user.getId());
            doctor.setSpecialization(registerDTO.getSpecialization());
            doctor.setIntroduction(registerDTO.getIntroduction());
            doctor.setTitle(registerDTO.getTitle());
            doctor.setScheduleRules("{}");
            doctorRepository.save(doctor);
        }
    }

    @Override
    public LoginVo login(LoginDTO loginDTO) throws BusinessException {
        User user = userRepository.findByUsername(loginDTO.getUsername())
                .orElseThrow(() -> new BusinessException("用户不存在"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException("密码错误");
        }

        String token = jwtUtil.generateToken(user);

        user.setLastLogin(new Date());
        userRepository.save(user);

        return LoginVo.builder()
                .token(token)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole())
                .build();
    }
}