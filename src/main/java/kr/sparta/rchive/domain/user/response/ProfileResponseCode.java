package kr.sparta.rchive.domain.user.response;

import kr.sparta.rchive.global.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ProfileResponseCode implements ResponseCode {
    // OK 200
    OK_GET_COMMENT(HttpStatus.OK, "PROFILE-001", "자신이 작성한 댓글 조회 성공"),
    OK_GET_BOOKMARK(HttpStatus.OK, "PROFILE-002", "북마크 조회 성공"),
    OK_GET_PROFILE(HttpStatus.OK, "PROFILE-005", "프로필 조회 성공"),
    OK_UPDATE_NICKNAME(HttpStatus.OK, "PROFILE-006", "닉네임 변경 성공"),
    OK_UPDATE_PASSWORD(HttpStatus.OK, "PROFILE-007", "비밀번호 변경 성공"),
    OK_UPDATE_PHONE(HttpStatus.OK, "PROFILE-008", "휴대폰 번호 변경 성공"),
    OK_UPDATE_PROFILE_IMG(HttpStatus.OK, "PROFILE-009", "프로필 이미지 변경 성공"),
    OK_SEARCH_BOOKMARK(HttpStatus.OK, "PROFILE-010", "북마크 검색 성공");

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
