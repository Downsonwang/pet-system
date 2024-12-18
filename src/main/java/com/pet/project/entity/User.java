package com.pet.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(name = "real_name")
    private String realName;

    private String phone;

    private String email;

    // 修改avatar字段，支持存储URL
    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private Date createTime;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
    private Date updateTime;

    @Column(name = "last_login", columnDefinition = "TIMESTAMP")
    private Date lastLogin;

    // 修改状态字段类型为Integer以匹配UserStatus枚举
    @Column(name = "status")
    private Integer status;

    public enum UserRole {
        CUSTOMER,
        DOCTOR,
        PHARMACIST,
        RECEPTIONIST,
        ADMIN
    }

    public enum UserStatus {
        NORMAL(1, "正常"),
        DISABLED(0, "禁用");

        private final Integer code;
        private final String description;

        UserStatus(Integer code, String description) {
            this.code = code;
            this.description = description;
        }

        public Integer getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    // 便利方法
    public boolean isDoctor() {
        return UserRole.DOCTOR.equals(this.role);
    }

    public boolean isAdmin() {
        return UserRole.ADMIN.equals(this.role);
    }

    public boolean isActive() {
        return UserStatus.NORMAL.getCode().equals(this.status);
    }

    // 更新最后登录时间
    public void updateLastLogin() {
        this.lastLogin = new Date();
    }

    // Avatar相关方法
    public void setAvatar(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getAvatar() {
        return this.avatarUrl;
    }

    // 时间更新方法
    @PrePersist
    protected void onCreate() {
        this.createTime = new Date();
        this.updateTime = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateTime = new Date();
    }
}