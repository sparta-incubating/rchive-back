package kr.sparta.rchive.global.execption;

import lombok.Getter;

@Getter
public class GlobalCustomException extends CustomException {
    public GlobalCustomException(GlobalExceptionCode exceptionCode) {
        super(exceptionCode.getHttpStatus(), exceptionCode.getErrorCode(),
                exceptionCode.getMessage());
    }
}
