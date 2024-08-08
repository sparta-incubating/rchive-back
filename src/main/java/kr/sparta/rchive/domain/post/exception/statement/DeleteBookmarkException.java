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
public enum DeleteBookmarkException implements ExceptionCode {
    NOT_FOUND_BOOKMARK(HttpStatus.NOT_FOUND, "POST-4004", "북마크를 찾을 수 없음");

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
