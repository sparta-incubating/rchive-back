package kr.sparta.rchive.domain.backoffice.respoonse;


import kr.sparta.rchive.domain.user.response.UserResponseCode;
import kr.sparta.rchive.global.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BackofficeResponseCode implements ResponseCode {
    // OK 200
    OK_GET_LAST_SELECT_ROLE(HttpStatus.OK, "BACKOFFICE-008", "백오피스: 마지막으로 선택한 권한 조회 성공"),
    OK_GET_PROFILE(HttpStatus.OK, "BACKOFFICE-009","백오피스: 프로필 조회 성공")
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