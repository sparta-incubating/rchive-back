package kr.sparta.rchive.global.execption;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum GlobalExceptionCode {

    /*  400 BAD_REQUEST : 잘못된 요청  */
    INVALID_VALUE(HttpStatus.BAD_REQUEST, "GLOBAL-001", "값이 유효하지 않습니다."),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "GLOBAL-002", "파라미터가 누락되었습니다."),

    /*  401 UNAUTHORIZED : 인증 안됨  */


    /*  403 FORBIDDEN : 권한 없음  */
    FORBIDDEN_DENIED_AUTHORITY(HttpStatus.FORBIDDEN, "GLOBAL-003" , "권한이 없습니다."),

    /*  404 NOT_FOUND : Resource 권한 없음, Resource 를 찾을 수 없음  */
    NOT_FOUND_ACCESS_DENIED(HttpStatus.NOT_FOUND,"GLOBAL-004","접근 권한이 없습니다."),

    /*  409 CONFLICT : Resource 중복  */
    CONFLICT_ALREADY_EXIST_USERID(HttpStatus.CONFLICT,"GLOBAL-005", "이미 존재하는 USERID 입니다."),

    /*  500 INTERNAL_SERVER_ERROR : 서버 에러  */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"GLOBAL-006", "내부 서버 에러입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
