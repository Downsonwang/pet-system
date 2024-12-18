package com.pet.project.service;

import com.pet.project.dto.LoginDTO;
import com.pet.project.dto.LoginVo;
import com.pet.project.dto.RegisterDTO;
import com.pet.project.exception.BusinessException;

public interface AuthService {
    void register(RegisterDTO registerDTO) throws BusinessException;
    LoginVo login(LoginDTO loginDTO) throws BusinessException;
}

