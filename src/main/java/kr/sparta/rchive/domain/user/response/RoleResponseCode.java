package kr.sparta.rchive.domain.user.response;

import kr.sparta.rchive.global.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum RoleResponseCode implements ResponseCode {
    // OK 200
    OK_REQUEST_ROLE(HttpStatus.OK, "ROLE-002","권한 요청 성공"),
    OK_GET_TRACK_NAME(HttpStatus.OK, "ROLE-005","트랙명 조회 성공"),
    OK_GET_TRACK_PERIOD(HttpStatus.OK, "ROLE-006", "트랙의 기수 조회 성공"),
    OK_GET_RESULT_ROLE_FIRST_LOGIN(HttpStatus.OK, "ROLE-007", "권한 신청 결과 조회 성공"),
    OK_GET_REQUEST_ROLE_FIRST_LOGIN(HttpStatus.OK, "ROLE-008", "권한 신청 여부 조회 성공"),
    OK_GET_LAST_SELECT_ROLE_BACKOFFICE(HttpStatus.OK, "ROLE-010", "백오피스에서 마지막으로 선택한 권한 조회 성공")
    ;

    private final HttpStatus httpStatus;
    private final String ResponseCode;
    private final String message;

    public HttpStatus getHttpStatus(UserResponseCode responseCode) {
        return responseCode.getHttpStatus();
    }

    public String getResponseCode(UserResponseCode responseCode) {
        return responseCode.getResponseCode();
    }

    public String getMessage(UserResponseCode responseCode) {
        return responseCode.getMessage();
    }

}