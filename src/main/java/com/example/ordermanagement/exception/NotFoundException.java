package com.example.ordermanagement.exception;

import com.example.ordermanagement.enums.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
