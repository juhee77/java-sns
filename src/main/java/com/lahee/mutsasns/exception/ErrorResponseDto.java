package com.lahee.mutsasns.exception;

import lombok.Data;

@Data
public class ErrorResponseDto {
    String errorCode;
    String errorMessage;

    public ErrorResponseDto(String errorCode, String message) {
        this.errorCode = errorCode;
        this.errorMessage = message;
    }

    public static ErrorResponseDto fromEntity(ErrorCode errorCode) {
        return new ErrorResponseDto(errorCode.code, errorCode.message);
    }
}
