package kr.sparta.rchive.global.execption;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum GlobalExceptionCode {

    /*  400 BAD_REQUEST : 잘못된 요청  */


    /*  401 UNAUTHORIZED : 인증 안됨  */


    /*  403 FORBIDDEN : 권한 없음  */
    FORBIDDEN_DENIED_AUTHORITY(HttpStatus.FORBIDDEN, "GLOBAL-001" , "권한이 없습니다."),

    /*  404 NOT_FOUND : Resource 권한 없음, Resource 를 찾을 수 없음  */
    NOT_FOUND_ACCESS_DENIED(HttpStatus.NOT_FOUND,"GLOBAL-002","접근 권한이 없습니다."),
    NOT_FOUND_FLASH_ERROR(HttpStatus.NOT_FOUND,"GLOBAL-003", "존재하지 않는 FLASH 입니다."),
    NOT_FOUND_USER_ERROR(HttpStatus.NOT_FOUND,"GLOBAL-004", "존재하지 않는 USER 입니다."),
    NOT_FOUND_COMMENT_ERROR(HttpStatus.NOT_FOUND,"GLOBAL-005", "존재하지 않는 COMMENT 입니다."),


    /*  409 CONFLICT : Resource 중복  */
    CONFLICT_ALREADY_EXIST_USERID(HttpStatus.CONFLICT,"GLOBAL-006", "이미 존재하는 USERID 입니다."),

    /*  500 INTERNAL_SERVER_ERROR : 서버 에러  */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"GLOBAL-007", "내부 서버 에러입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
