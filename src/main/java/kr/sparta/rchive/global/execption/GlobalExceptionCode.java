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

    /*  401 UNAUTHORIZED : 인증 안됨  */

    /*  403 FORBIDDEN : 권한 없음  */
    FORBIDDEN_DENIED_AUTHORITY(HttpStatus.FORBIDDEN, "GLOBAL-003", "권한이 없음"),

    /*  404 NOT_FOUND : Resource 권한 없음, Resource 를 찾을 수 없음  */
    NOT_FOUND_ACCESS_DENIED(HttpStatus.NOT_FOUND, "GLOBAL-004", "접근 권한이 없음"),

    /*  409 CONFLICT : Resource 중복  */

    /*  500 INTERNAL_SERVER_ERROR : 서버 에러  */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"GLOBAL-005", "내부 서버 에러"),
    INTERNAL_SERVER_ERROR_FILE_SIZE_OVERFLOW(HttpStatus.INTERNAL_SERVER_ERROR, "GLOBAL-009", "파일 용량 초과");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
