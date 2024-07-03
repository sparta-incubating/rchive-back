package kr.sparta.rchive.domain.post.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum PostExceptionCode {
    /*  400 BAD_REQUEST : 잘못된 요청  */

    /*  401 UNAUTHORIZED : 인증 안됨  */

    /*  403 FORBIDDEN : 권한 없음  */
    FORBIDDEN_NOT_PERMISSION(HttpStatus.FORBIDDEN, "POST-004", "해당 트랙을 조회할 권한이 존재하지 않음"),

    /*  404 NOT_FOUND : Resource 권한 없음, Resource 를 찾을 수 없음  */
    NOT_FOUND_POST_NOT_EXIST(HttpStatus.NOT_FOUND, "POST-002", "해당 자료는 존재하지 않음"),
    NOT_FOUND_TAG_NOT_EXIST(HttpStatus.NOT_FOUND, "POST-003", "해당 태그는 존재하지 않음"),

    /*  409 CONFLICT : Resource 중복  */

    CONFLICT_TAG(HttpStatus.CONFLICT, "POST-001", "중복된 태그");

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
