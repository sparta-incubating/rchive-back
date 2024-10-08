package kr.sparta.rchive.global.execption;

import java.util.HashMap;
import java.util.Map;


import kr.sparta.rchive.global.response.CommonResponseDto;
import kr.sparta.rchive.global.s3.exception.S3ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> runtimeExceptionHandler(RuntimeException exception) {
        log.error("RuntimeException: ", exception);
        return ResponseEntity.status(GlobalExceptionCode.INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(CommonResponseDto.of(GlobalExceptionCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> httpMessageNotReadableExceptionHandler(
            HttpMessageNotReadableException exception) {
        log.error("HttpMessageNotReadableException: ", exception);
        return ResponseEntity.status(GlobalExceptionCode.BAD_REQUEST_INVALID_PARAMETER.getHttpStatus())
                .body(CommonResponseDto.of(GlobalExceptionCode.BAD_REQUEST_INVALID_PARAMETER));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> IllegalArgumentHandler(IllegalArgumentException exception) {
        log.error("IllegalArgumentException: ", exception);
        return ResponseEntity.status(GlobalExceptionCode.BAD_REQUEST_INVALID_PARAMETER.getHttpStatus())
                .body(CommonResponseDto.of(GlobalExceptionCode.BAD_REQUEST_INVALID_PARAMETER));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> methodArgumentNotValidHandler(
            MethodArgumentNotValidException exception) {
        log.error("handleMethodArgumentNotValidException: ", exception);
        BindingResult bindingResult = exception.getBindingResult();
        Map<String, String> errors = new HashMap<>();
        bindingResult.getAllErrors().forEach(
                error -> errors.put(((FieldError) error).getField(), error.getDefaultMessage()));

        return ResponseEntity.status(GlobalExceptionCode.BAD_REQUEST_INVALID_PARAMETER.getHttpStatus())
                .body(CommonResponseDto.of(GlobalExceptionCode.BAD_REQUEST_INVALID_PARAMETER));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> customExceptionHandler(CustomException exception) {
        log.error("CustomException: ", exception);
        return ResponseEntity.status(exception.getHttpStatus())
                .body(CommonResponseDto.of(exception));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> uploadSizeExceededExceptionHandler(CustomException exception) {
        log.error("uploadSizeExceededException: ", exception);
        return ResponseEntity.status(GlobalExceptionCode.INTERNAL_SERVER_ERROR_FILE_SIZE_OVERFLOW.getHttpStatus())
            .body(CommonResponseDto.of(GlobalExceptionCode.INTERNAL_SERVER_ERROR_FILE_SIZE_OVERFLOW));
    }
}

