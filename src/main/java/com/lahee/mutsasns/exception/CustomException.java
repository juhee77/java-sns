package com.lahee.mutsasns.exception;

public class CustomException extends RuntimeException {
    ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public CustomException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CustomException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public CustomException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public CustomException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, ErrorCode errorCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = errorCode;
    }
}
