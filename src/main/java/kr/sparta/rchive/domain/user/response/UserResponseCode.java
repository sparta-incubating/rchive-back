package kr.sparta.rchive.domain.user.response;

import kr.sparta.rchive.global.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserResponseCode implements ResponseCode {
    // OK 200
    OK_SIGNUP(HttpStatus.OK, "USER-001", "회원가입 성공"),
    OK_LOGOUT(HttpStatus.OK, "USER-004", "로그아웃 성공"),
    OK_REISSUE(HttpStatus.OK, "USER-005", "토큰 재발급 성공"),
    OK_DELETE_USER(HttpStatus.OK, "USER-006", "회원 탈퇴 성공"),
    OK_OVERLAP_EMAIL(HttpStatus.OK, "USER-007", "이메일 중복 여부 조회 성공"),
    OK_OVERLAP_NICKNAME(HttpStatus.OK, "USER-008", "닉네임 중복 여부 조회 성공"),
    OK_ACCESS_TOKEN_IS_EXPIRED(HttpStatus.OK, "USER-009", "유저 Access Token 만료 여부 조회 성공"),
    OK_SEND_AUTH_PHONE(HttpStatus.OK, "USER-010", "휴대폰 인증 전송 성공"),
    OK_VALID_AUTH_PHONE(HttpStatus.OK, "USER-011", "휴대폰 인증 확인 성공"),
    OK_FIND_EMAIL(HttpStatus.OK, "USER-012", "이메일 찾기 성공"),
    OK_FIND_PASSWORD(HttpStatus.OK, "USER-013", "비밀번호 찾기 성공");

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