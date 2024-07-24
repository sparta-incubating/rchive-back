package kr.sparta.rchive.domain.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum TrackExceptionCode {
    /*  400 BAD_REQUEST : 잘못된 요청  */

    /*  401 UNAUTHORIZED : 인증 안됨  */

    /*  403 FORBIDDEN : 권한 없음  */

    /*  404 NOT_FOUND : Resource 권한 없음, Resource 를 찾을 수 없음  */
    NOT_FOUND_TRACK(HttpStatus.NOT_FOUND, "TRACK-4001", "트랙을 찾을 수 없음")

    /*  409 CONFLICT : Resource 중복  */;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
