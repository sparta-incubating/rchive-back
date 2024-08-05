package kr.sparta.rchive.global.execption.statement;

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
public enum SignupException implements ExceptionCode {
    /*  400 BAD_REQUEST : 잘못된 요청  */
    BAD_REQUEST_MANAGER_NICKNAME(HttpStatus.BAD_REQUEST, "USER-0002", "매니저 닉네임 입력 불가"),
    BAD_REQUEST_DISAGREE_TERMS(HttpStatus.BAD_REQUEST, "USER-0003", "이용약관 미동의"),

    /*  409 CONFLICT : Resource 중복  */
    CONFLICT_EMAIL(HttpStatus.CONFLICT, "USER-9001", "이메일 중복"),
    CONFLICT_NICKNAME(HttpStatus.CONFLICT, "USER-9002", "닉네임 중복")
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
