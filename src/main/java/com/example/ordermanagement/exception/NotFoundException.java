package com.example.ordermanagement.exception;

import com.example.ordermanagement.enums.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public NotFoundException() {
        super(ErrorCode.MEAL_NOT_FOUND.getMessage());
        this.errorCode = ErrorCode.MEAL_NOT_FOUND;
    }
}
