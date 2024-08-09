package kr.sparta.rchive.domain.post.exception.statement;

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
public enum SearchPostByTagException implements ExceptionCode {
    FORBIDDEN_TRACK_NOT_PERMISSION(HttpStatus.FORBIDDEN, "TRACK-3001", "트랙 열람권한 없음"),
    NOT_FOUND_TRACK(HttpStatus.NOT_FOUND, "TRACK-4001", "트랙을 찾을 수 없음"),
    BAD_REQUEST_NO_ROLE(HttpStatus.BAD_REQUEST, "ROLE-0001", "유저가 속한 트랙 없음"),
    FORBIDDEN_ROLE(HttpStatus.FORBIDDEN, "ROLE-3001", "해당 권한 접근 불가");

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
