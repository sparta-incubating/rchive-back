package kr.sparta.rchive.global.execption;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;


public interface ExceptionCode {

    public ExceptionReason getExceptionReason();

    String getExplainException() throws NoSuchFieldException;
}
