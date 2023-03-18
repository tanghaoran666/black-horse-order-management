package com.example.ordermanagement.exception;

import com.example.ordermanagement.enums.ErrorCode;
import lombok.Getter;

@Getter
public class ServerUnavailableException extends RuntimeException {
    private final ErrorCode errorCode;

    public ServerUnavailableException() {
        super(ErrorCode.SERVER_UNAVAILABLE.getMessage());
        this.errorCode = ErrorCode.SERVER_UNAVAILABLE;
    }
}
