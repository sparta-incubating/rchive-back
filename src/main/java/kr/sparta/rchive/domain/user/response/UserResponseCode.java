package kr.sparta.rchive.domain.user.response;

import kr.sparta.rchive.global.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserResponseCode implements ResponseCode {
    // OK 200
    OK_SIGNUP(HttpStatus.OK, "회원가입 성공"),
    OK_LOGOUT(HttpStatus.OK, "로그아웃 성공"),
    OK_REISSUE(HttpStatus.OK, "토큰 재발급 성공")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public HttpStatus getHttpStatus(UserResponseCode responseCode) {
        return responseCode.getHttpStatus();
    }

    public String getMessage(UserResponseCode responseCode) {
        return responseCode.getMessage();
    }

}