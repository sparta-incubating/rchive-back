package kr.sparta.rchive.domain.user.exception;

import kr.sparta.rchive.global.execption.CustomException;
import lombok.Getter;

@Getter
public class TrackCustomException extends CustomException {

    public TrackCustomException(TrackExceptionCode exceptionCode) {
        super(exceptionCode.getHttpStatus(), exceptionCode.getErrorCode(),
                exceptionCode.getMessage());
    }
}