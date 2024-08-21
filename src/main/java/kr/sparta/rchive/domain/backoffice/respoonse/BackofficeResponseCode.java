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
    OK_GET_USER_TRACK_ROLE_REQUEST_LIST(HttpStatus.OK, "BACKOFFICE-001",
            "백오피스: 유저의 트랙 권한 신청 목록 조회"),
    OK_APPROVE_USER_TRACK_ROLE(HttpStatus.OK, "BACKOFFICE-002", "백오피스: 유저의 트랙 권한 수락 성공"),
    OK_REJECT_USER_TRACK_ROLE(HttpStatus.OK, "BACKOFFICE-003", "백오피스: 유저의 트랙 권한 거절 성공"),
    OK_GET_USER_TRACK_ROLE_REQUEST_COUNT(HttpStatus.OK, "BACKOFFICE-004", "백오피스: 유저의 트랙 권한 신청 건수 조회 성공"),
    OK_USER_TRACK_PERMISSION(HttpStatus.OK, "BACKOFFICE-005", "백오피스: 유저 트랙 열람 권한 수락"),
    OK_SEARCH_POST_LIST(HttpStatus.OK, "BACKOFFICE-006", "백오피스: 교육자료 검색"),
    OK_GET_LAST_SELECT_ROLE(HttpStatus.OK, "BACKOFFICE-007", "백오피스: 마지막으로 선택한 권한 조회 성공"),
    OK_GET_PROFILE(HttpStatus.OK, "BACKOFFICE-008", "백오피스: 프로필 조회 성공"),
    OK_USER_TRACK_REJECTION(HttpStatus.OK, "BACKOFFICE-009", "백오피스: 유저 트랙 열람 권한 거절 성공"),
    OK_GET_POST_DETAIL_IN_BACKOFFICE(HttpStatus.OK, "BACKOFFICE-010", "백오피스: 게시물 수정 시 기존 내용 조회 성공")
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