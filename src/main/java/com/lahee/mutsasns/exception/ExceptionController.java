package com.lahee.mutsasns.exception;

import com.lahee.mutsasns.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ExceptionController {
    @ExceptionHandler(CustomException.class)
    public ApiResponse<ErrorResponseDto> handleException(CustomException e) {
        ErrorCode errorCode = e.errorCode;
        ErrorResponseDto errorResponseDto = ErrorResponseDto.fromEntity(errorCode);
        return new ApiResponse<>(HttpStatus.valueOf(errorCode.status), errorResponseDto);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Map<String, Object>> handleValidationException(MethodArgumentNotValidException exception) {
        Map<String, Object> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ApiResponse<>(HttpStatus.BAD_REQUEST, errors);
    }


    //처리되지 못한 예외 사항을 체크하기 위해서 테스트 용도
    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<ErrorResponseDto> handleOtherError(RuntimeException e) {
        ApiResponse<ErrorResponseDto> response = new ApiResponse<>(HttpStatus.BAD_REQUEST, new ErrorResponseDto("", String.format("예외 처리 하지 않음 : %s ", e.getMessage())));
        return response;
    }
}
