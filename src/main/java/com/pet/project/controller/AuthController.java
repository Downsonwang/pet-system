// com.pet.project.controller.AuthController
package com.pet.project.controller;

import com.pet.project.common.Result;
import com.pet.project.dto.LoginDTO;
import com.pet.project.dto.LoginVo;
import com.pet.project.dto.RegisterDTO;
import com.pet.project.entity.User;
import com.pet.project.exception.BusinessException;
import com.pet.project.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public Result<?> register(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam("avatar") MultipartFile avatar,
            @RequestParam("role") User.UserRole role,  // 直接使用 UserRole 枚举
            @RequestParam(value = "specialization", required = false) String specialization,
            @RequestParam(value = "introduction", required = false) String introduction,
            @RequestParam(value = "title", required = false) String title) {
        try {
            RegisterDTO registerDTO = new RegisterDTO();
            registerDTO.setUsername(username);
            registerDTO.setPassword(password);
            registerDTO.setEmail(email);
            registerDTO.setAvatar(avatar);
            registerDTO.setRole(role);

            // 如果角色为医生，则设置医生信息
            if (User.UserRole.DOCTOR.equals(role)) {  // 使用枚举比较
                if (specialization == null || title == null) {
                    return Result.error("医生注册需要提供专业和职称信息");
                }
                registerDTO.setSpecialization(specialization);
                registerDTO.setIntroduction(introduction);
                registerDTO.setTitle(title);
            }else  if (User.UserRole.PHARMACIST.equals(role)) {
                // 可以添加药房工作人员特有的验证或处理
                // 比如验证工号格式等
                if (!username.startsWith("PH")) {  // 假设药房工作人员工号以PH开头
                    return Result.error("药房工作人员工号格式不正确，应以PH开头");
                }

            }


            // 如果角色为药师,则设置医药管理员信心

            authService.register(registerDTO);
            return Result.success(null);
        } catch (BusinessException e) {
            log.error("注册失败", e);
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("注册异常", e);
            return Result.error("系统异常，请稍后重试");
        }
    }

    @PostMapping("/login")
    public Result<?> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {
        try {
            LoginDTO loginDTO = new LoginDTO();
            loginDTO.setUsername(username);
            loginDTO.setPassword(password);

            LoginVo loginVO = authService.login(loginDTO);
            return Result.success(loginVO);
        } catch (BusinessException e) {
            log.error("登录失败", e);
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("登录异常", e);
            return Result.error("系统异常，请稍后重试");
        }
    }
}