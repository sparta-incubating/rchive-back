package kr.sparta.rchive.domain.user.exception.statement.user;

import kr.sparta.rchive.global.execption.ExceptionCode;
import kr.sparta.rchive.global.execption.ExceptionReason;
import kr.sparta.rchive.global.execption.ExplainException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.Objects;

@AllArgsConstructor
@Getter
public enum ReissueException implements ExceptionCode {
    BAD_REQUEST_REFRESH_TOKEN_NULL(HttpStatus.BAD_REQUEST, "USER-0005", "리프레시 토큰 없음"),
    BAD_REQUEST_REFRESH_TOKEN_NOT_MATCH(HttpStatus.BAD_REQUEST, "USER-0006", "리프레시 토큰이 일치하지 않음"),
    BAD_REQUEST_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "USER-0007", "토큰 시간 만료");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

    @Override
    public ExceptionReason getExceptionReason() {
        return ExceptionReason.builder()
                .errorCode(errorCode)
                .httpStatus(httpStatus)
                .message(message).build();
    }

    @Override
    public String getExplainException() throws NoSuchFieldException {
        Field field = this.getClass().getField(this.name());
        ExplainException annotation = field.getAnnotation(ExplainException.class);
        return Objects.nonNull(annotation) ? annotation.value() : this.getMessage();
    }
}
