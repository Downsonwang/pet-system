// com.pet.project.entity.UserStatus
package com.pet.project.entity;

import lombok.Getter;

@Getter
public enum UserStatus {
    NORMAL(1, "正常"),
    DISABLED(0, "禁用");

    private final Integer code;
    private final String desc;

    UserStatus(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
