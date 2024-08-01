package kr.sparta.rchive.domain.post.exception;

import java.lang.reflect.Field;
import java.util.Objects;
import kr.sparta.rchive.global.execption.ExceptionCode;
import kr.sparta.rchive.global.execption.ExceptionReason;
import kr.sparta.rchive.global.execption.ExplainException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum PostExceptionCode implements ExceptionCode {
    /*  400 BAD_REQUEST : 잘못된 요청  */
    BAD_REQUEST_NOT_TRACK_TUTOR(HttpStatus.BAD_REQUEST, "POST-0001", "해당 트랙의 튜터가 아님"),
    /*  401 UNAUTHORIZED : 인증 안됨  */

    /*  403 FORBIDDEN : 권한 없음  */

    /*  404 NOT_FOUND : Resource 권한 없음, Resource 를 찾을 수 없음  */
    NOT_FOUND_POST(HttpStatus.NOT_FOUND, "POST-4001", "자료를 찾을 수 없음"),
    NOT_FOUND_TAG(HttpStatus.NOT_FOUND, "POST-4002", "태그를 찾을 수 없음"),
    NOT_FOUND_CONTENT(HttpStatus.NOT_FOUND, "POST-4003", "게시물 내용을 찾을 수 없음"),
    NOT_FOUND_BOOKMARK(HttpStatus.NOT_FOUND, "POST-4004", "북마크를 찾을 수 없음"),
    NOT_FOUND_TUTOR(HttpStatus.NOT_FOUND, "POST-4005", "튜터를 찾을 수 없음"),

    /*  409 CONFLICT : Resource 중복  */
    CONFLICT_TAG(HttpStatus.CONFLICT, "POST-9001", "중복된 태그"),
    CONFLICT_BOOKMARK(HttpStatus.CONFLICT, "POST-9002", "중복된 북마크"),
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
