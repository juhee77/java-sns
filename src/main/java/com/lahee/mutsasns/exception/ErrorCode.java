package com.lahee.mutsasns.exception;

public enum ErrorCode {
    ERROR_BAD_REQUEST(400, "ERR_BAD_REQUEST", "Bad request"),
    ERROR_UNAUTHORIZED(401, "ERR_UNAUTHORIZED", "Unauthorized access"),
    ERROR_FORBIDDEN(403, "ERR_FORBIDDEN", "Access forbidden"),
    ERROR_NOT_FOUND(404, "ERR_NOT_FOUND", "Resource not found"),
    ERROR_METHOD_NOT_ALLOWED(405, "ERR_METHOD_NOT_ALLOWED", "Method not allowed"),
    ERROR_NOT_ACCEPTABLE(406, "ERR_NOT_ACCEPTABLE", "Not acceptable"),
    ERROR_CONFLICT(409, "ERR_CONFLICT", "Conflict with existing resource"),
    ERROR_LENGTH_REQUIRED(411, "ERR_LENGTH_REQUIRED", "Length required"),
    ERROR_PRECONDITION_FAILED(412, "ERR_PRECONDITION_FAILED", "Precondition failed"),
    ERROR_UNSUPPORTED_MEDIA(415, "ERR_UNSUPPORTED_MEDIA", "Unsupported media type"),
    ERROR_TOO_MANY_REQUESTS(429, "ERR_TOO_MANY_REQUESTS", "Too many requests"),
    ERROR_INTERNAL_SERVER(500, "ERR_INTERNAL_SERVER", "Internal server error"),
    ERROR_SERVICE_UNAVAILABLE(503, "ERR_SERVICE_UNAVAILABLE", "Service temporarily unavailable"),
    ERROR_GATEWAY_TIMEOUT(504, "ERR_GATEWAY_TIMEOUT", "Gateway timeout"),
    ERROR_NETWORK_AUTHENTICATION_REQUIRED(511, "ERR_NETWORK_AUTH_REQUIRED", "Network authentication required"),

    USER_NOT_FOUND(404, "ERR_NOT_FOUND", "찾을 수 없는 유저 입니다."),
    FILE_NOT_FOUND(404, "ERR_NOT_FOUND", "찾을 수 없는 파일 입니다."),
    COMMENT_NOT_FOUND(404, "ERR_NOT_FOUND", "찾을 수 없는 코멘트 입니다."),
    POST_NOT_FOUND(404, "ERR_NOT_FOUND", "찾을 수 없는 포스트 입니다."),

    INVALID_PASSWORD(404, "", "패스워드가 일치하지 않습니다."),
    ALREADY_USED_USERNAME(400,"" , "이미 사용된 유저이름 입니다"),
    ERROR_MY_POST(401,"ERR_UNAUTHORIZED" ,"자신이 작성한 게시글에는 좋아요가 불가능합니다." );


    int status;
    String code;
    String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
