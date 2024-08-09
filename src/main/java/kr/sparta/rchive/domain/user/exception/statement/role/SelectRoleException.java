package kr.sparta.rchive.domain.user.exception.statement.role;

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
public enum SelectRoleException implements ExceptionCode {
    BAD_REQUEST_NO_ROLE(HttpStatus.BAD_REQUEST, "ROLE-0001", "유저가 속한 트랙 없음"),
    BAD_REQUEST_NO_PARAMETER_PERIOD(HttpStatus.BAD_REQUEST, "ROLE-0003", "파라미터 누락: period"),
    NOT_FOUND_ROLE(HttpStatus.NOT_FOUND, "ROLE-4001", "권한을 찾을 수 없음"),
    NOT_FOUND_TRACK(HttpStatus.NOT_FOUND, "TRACK-4001", "트랙을 찾을 수 없음")
    ;

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
