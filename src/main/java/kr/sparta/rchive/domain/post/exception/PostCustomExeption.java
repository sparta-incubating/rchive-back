package kr.sparta.rchive.domain.post.exception;

import kr.sparta.rchive.global.execption.CustomException;
import lombok.Getter;

@Getter
public class PostCustomExeption extends CustomException {

    public PostCustomExeption(PostExceptionCode exceptionCode) {
        super(exceptionCode.getHttpStatus(), exceptionCode.getErrorCode(),
                exceptionCode.getMessage());
    }
}
