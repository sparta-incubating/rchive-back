package kr.sparta.rchive.global.execption;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum GlobalExceptionCode {

    /*  400 BAD_REQUEST : 잘못된 요청  */
    BAD_REQUEST_INVALID_VALUE(HttpStatus.BAD_REQUEST, "GLOBAL-001", "유효하지 않은 값"),
    BAD_REQUEST_INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "GLOBAL-002", "파라미터 누락"),
    BAD_REQUEST_REFRESH_TOKEN_NULL(HttpStatus.BAD_REQUEST,"GLOBAL-007","리프레시 토큰 없음"),
    BAD_REQUEST_REFRESH_NOT_MATCH(HttpStatus.BAD_REQUEST,"GLOBAL-008","리프레시 토큰이 일치하지 않음"),
    BAD_REQUEST_TOKEN_EXPIRED(HttpStatus.BAD_REQUEST,"GLOBAL-009","토큰 시간 만료"),

    /*  401 UNAUTHORIZED : 인증 안됨  */

    /*  403 FORBIDDEN : 권한 없음  */
    FORBIDDEN_DENIED_AUTHORITY(HttpStatus.FORBIDDEN, "GLOBAL-003" , "권한이 없음"),

    /*  404 NOT_FOUND : Resource 권한 없음, Resource 를 찾을 수 없음  */
    NOT_FOUND_ACCESS_DENIED(HttpStatus.NOT_FOUND,"GLOBAL-004","접근 권한이 없음"),

    /*  409 CONFLICT : Resource 중복  */
    CONFLICT_ALREADY_EXIST_USERID(HttpStatus.CONFLICT,"GLOBAL-005", "이미 존재하는 USER ID"),

    /*  500 INTERNAL_SERVER_ERROR : 서버 에러  */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"GLOBAL-006", "내부 서버 에러")
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
