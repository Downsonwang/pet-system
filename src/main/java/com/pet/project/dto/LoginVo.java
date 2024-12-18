package com.pet.project.dto;

import com.pet.project.entity.User;
import com.pet.project.entity.UserRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginVo {
    private String token;
    private Long userId;
    private String username;
    private String email;
    private String avatarUrl;
    private User.UserRole role; // 角色信息

}
