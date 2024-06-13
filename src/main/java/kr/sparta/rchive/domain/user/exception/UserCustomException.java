package kr.sparta.rchive.domain.user.exception;

import kr.sparta.rchive.global.execption.CustomException;
import lombok.Getter;

@Getter
public class UserCustomException extends CustomException {
    public UserCustomException(UserExceptionCode exceptionCode) {
        super(exceptionCode.getHttpStatus(), exceptionCode.getErrorCode(),
                exceptionCode.getMessage());
    }
}
