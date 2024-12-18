package com.pet.project.exception;

// 6. 异常类
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}