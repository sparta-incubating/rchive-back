package kr.sparta.rchive.domain.user.exception;

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
public enum UserExceptionCode implements ExceptionCode {
    /*  400 BAD_REQUEST : 잘못된 요청  */
    @ExplainException("이메일이 데이터베이스에 존재하지 않을 때 나는 오류")
    BAD_REQUEST_EMAIL(HttpStatus.BAD_REQUEST, "USER-0001", "존재하지 않는 이메일"),
    BAD_REQUEST_STUDENT_NICKNAME_NULL(HttpStatus.BAD_REQUEST, "USER-0002", "학생 닉네임 입력 필수"),
    BAD_REQUEST_DISAGREE_TERMS(HttpStatus.BAD_REQUEST, "USER-0003", "이용약관 미동의"),
    BAD_REQUEST_NO_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "USER-0004", "비밀번호가 일치하지 않음"),
    BAD_REQUEST_REFRESH_TOKEN_NULL(HttpStatus.BAD_REQUEST, "USER-0005", "리프레시 토큰 없음"),
    BAD_REQUEST_REFRESH_TOKEN_NOT_MATCH(HttpStatus.BAD_REQUEST, "USER-0006", "리프레시 토큰이 일치하지 않음"),
    BAD_REQUEST_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "USER-0007", "토큰 시간 만료"),
    BAD_REQUEST_PHONE_AUTH_EXPIRED(HttpStatus.BAD_REQUEST, "USER-0008", "휴대폰 인증 시간 만료"),
    BAD_REQUEST_PHONE_AUTH(HttpStatus.BAD_REQUEST, "USER-0009", "휴대폰 인증 번호가 일치하지 않음"),
    BAD_REQUEST_USER(HttpStatus.BAD_REQUEST, "USER-0010", "존재하지 않는 유저"),
    BAD_REQUEST_SAME_NICKNAME(HttpStatus.BAD_REQUEST, "USER-0011", "기존의 닉네임과 같음"),
    BAD_REQUEST_NICKNAME(HttpStatus.BAD_REQUEST, "USER-0012", "닉네임 없음"),
    BAD_REQUEST_PROFILE_IMG(HttpStatus.BAD_REQUEST, "USER-0012", "프로필이미지 없음"),

    /*  401 UNAUTHORIZED : 인증 안됨  */

    /*  403 FORBIDDEN : 권한 없음  */

    /*  404 NOT_FOUND : Resource 권한 없음, Resource 를 찾을 수 없음  */

    /*  409 CONFLICT : Resource 중복  */
    CONFLICT_EMAIL(HttpStatus.CONFLICT, "USER-9001", "이메일 중복"),
    CONFLICT_NICKNAME(HttpStatus.CONFLICT, "USER-9002", "닉네임 중복");

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
