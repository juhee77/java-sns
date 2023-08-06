package com.lahee.mutsasns.dto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;


@Slf4j
public class ApiResponse<T> extends ResponseEntity{

    public ApiResponse(HttpStatusCode status) {
        super(status);
    }

    public ApiResponse(HttpStatus httpStatus, T data) {
        super(data,httpStatus);
    }

    public static <T> ApiResponse<T> success(T data) {
        log.info(data.toString());
        return new ApiResponse<T>(HttpStatus.OK, data);
    }
}