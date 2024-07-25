package kr.sparta.rchive.global.execption;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ExceptionReason {

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
