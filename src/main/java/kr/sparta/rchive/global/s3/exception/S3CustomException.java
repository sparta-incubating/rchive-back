package kr.sparta.rchive.global.s3.exception;

import kr.sparta.rchive.global.execption.CustomException;
import lombok.Getter;

@Getter
public class S3CustomException extends CustomException {

    public S3CustomException(S3ExceptionCode exceptionCode) {
        super(exceptionCode.getHttpStatus(), exceptionCode.getErrorCode(),
                exceptionCode.getMessage());
    }
}
