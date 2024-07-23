package kr.sparta.rchive.domain.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum RoleExceptionCode {
    /*  400 BAD_REQUEST : 잘못된 요청  */
    BAD_REQUEST_NO_ROLE(HttpStatus.BAD_REQUEST, "ROLE-0001", "유저가 속한 트랙 없음"),
    BAD_REQUEST_NO_ROLE_REQUEST_LIST(HttpStatus.BAD_REQUEST, "ROLE-0002", "유저가 신청한 트랙 내역 없음"),
    BAD_REQUEST_NO_PARAMETER_PERIOD(HttpStatus.BAD_REQUEST, "ROLE-0003", "파라미터 누락: period"),

    /*  401 UNAUTHORIZED : 인증 안됨  */

    /*  403 FORBIDDEN : 권한 없음  */
    FORBIDDEN_USER_ROLE_USER(HttpStatus.FORBIDDEN, "ROLE-3001", "일반 유저 접근 불가"),
    FORBIDDEN_ROLE_NOT_ACCESS(HttpStatus.FORBIDDEN, "ROLE-3002", "해당 권한 접근 불가"),
    FORBIDDEN_TRACK_NOT_ACCESS(HttpStatus.BAD_REQUEST, "ROLE-3003", "해당 트랙 접근 불가"),

    /*  404 NOT_FOUND : Resource 권한 없음, Resource 를 찾을 수 없음  */
    NOT_FOUND_ROLE_REQUEST(HttpStatus.NOT_FOUND, "ROLE-4001", "권한 신청 없음")

    /*  409 CONFLICT : Resource 중복  */
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
