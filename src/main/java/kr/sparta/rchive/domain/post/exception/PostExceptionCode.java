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

    /*  404 NOT_FOUND : Resource 권한 없음, Resource 를 찾을 수 없음  */

    /*  409 CONFLICT : Resource 중복  */

    /*  500 INTERNAL_SERVER_ERROR : 서버 에러  */
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
