package kr.sparta.rchive.domain.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum UserExceptionCode {
    /*  400 BAD_REQUEST : 잘못된 요청  */
    BAD_REQUEST_EMAIL(HttpStatus.BAD_REQUEST, "USER-0001", "존재하지 않는 이메일"),
    BAD_REQUEST_MANAGER_NICKNAME(HttpStatus.BAD_REQUEST, "USER-0002", "매니저 닉네임 입력 불가"),
    BAD_REQUEST_DISAGREE_TERMS(HttpStatus.BAD_REQUEST, "USER-0003", "이용약관 미동의"),
    BAD_REQUEST_NO_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "USER-0004", "비밀번호가 일치하지 않음"),
    BAD_REQUEST_REFRESH_TOKEN_NULL(HttpStatus.BAD_REQUEST, "USER-0005", "리프레시 토큰 없음"),
    BAD_REQUEST_REFRESH_TOKEN_NOT_MATCH(HttpStatus.BAD_REQUEST, "USER-0006", "리프레시 토큰이 일치하지 않음"),
    BAD_REQUEST_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "USER-0007", "토큰 시간 만료"),

    /*  401 UNAUTHORIZED : 인증 안됨  */

    /*  403 FORBIDDEN : 권한 없음  */

    /*  404 NOT_FOUND : Resource 권한 없음, Resource 를 찾을 수 없음  */

    /*  409 CONFLICT : Resource 중복  */
    CONFLICT_EMAIL(HttpStatus.CONFLICT, "USER-9001", "이메일 중복"),
    CONFLICT_NICKNAME(HttpStatus.CONFLICT, "USER-9002", "닉네임 중복");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
