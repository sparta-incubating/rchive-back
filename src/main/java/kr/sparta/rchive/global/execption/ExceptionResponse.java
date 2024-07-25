package kr.sparta.rchive.global.execption;

import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExceptionResponse {

    private final boolean success = false;
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
//    private final String path;

    public ExceptionResponse(ExceptionReason exceptionReason) {
        this.httpStatus = exceptionReason.getHttpStatus();
        this.errorCode = exceptionReason.getErrorCode();
        this.message = exceptionReason.getMessage();
//        this.path = path;
    }

    public ExceptionResponse(HttpStatus httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
//        this.path = path;
    }
}