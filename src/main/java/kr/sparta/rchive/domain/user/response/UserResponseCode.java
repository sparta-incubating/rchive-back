package kr.sparta.rchive.domain.user.response;

import kr.sparta.rchive.global.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserResponseCode implements ResponseCode {
    // OK 200
    /* USER API */
    OK_SIGNUP(HttpStatus.OK, "USER-001", "회원가입 성공"),
    OK_LOGOUT(HttpStatus.OK, "USER-004", "로그아웃 성공"),
    OK_REISSUE(HttpStatus.OK, "USER-005", "토큰 재발급 성공"),
    OK_DELETE_USER(HttpStatus.OK, "USER-006", "회원 탈퇴 성공"),
    OK_OVERLAP_EMAIL(HttpStatus.OK, "USER-009", "이메일 중복 여부 조회 성공"),
    OK_OVERLAP_NICKNAME(HttpStatus.OK, "USER-010", "닉네임 중복 여부 조회 성공"),

    /* ROLE API */
    OK_REQUEST_ROLE(HttpStatus.OK, "ROLE-002","권한 요청 성공"),
    OK_GET_TRACK_NAME(HttpStatus.OK, "ROLE-005","트랙명 조회 성공"),
    OK_GET_TRACK_PERIOD(HttpStatus.OK, "ROLE-006", "트랙의 기수 조회 성공"),
    OK_GET_REQUEST_ROLE(HttpStatus.OK, "ROLE-008", "권한 신청 여부 조회 성공")
    ;

    private final HttpStatus httpStatus;
    private final String ResponseCode;
    private final String message;

    public HttpStatus getHttpStatus(UserResponseCode responseCode) {
        return responseCode.getHttpStatus();
    }

    public String getMessage(UserResponseCode responseCode) {
        return responseCode.getMessage();
    }

}