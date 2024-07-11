package kr.sparta.rchive.domain.user.exception;

import kr.sparta.rchive.global.execption.CustomException;
import lombok.Getter;

@Getter
public class RoleCustomException extends CustomException {
    public RoleCustomException(RoleExceptionCode exceptionCode) {
        super(exceptionCode.getHttpStatus(), exceptionCode.getErrorCode(),
                exceptionCode.getMessage());
    }
}
