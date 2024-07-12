package kr.sparta.rchive.domain.post.response;

import kr.sparta.rchive.global.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PostResponseCode implements ResponseCode {

    // OK 200
    OK_CREATE_POST(HttpStatus.OK, "POST-001", "교육자료 생성 성공"),
    OK_MODIFY_POST(HttpStatus.OK, "POST-002", "교육자료 수정 성공"),
    OK_DELETE_POST(HttpStatus.OK, "POST-003", "교육자료 삭제 성공"),
    OK_SEARCH_POST(HttpStatus.OK, "POST-004", "교육자료 검색 성공"),
    OK_GET_CATEGORY_POST(HttpStatus.OK, "POST-005", "교육자료 카테고리 별 조회 성공"),
    OK_GET_SINGLE_POST(HttpStatus.OK, "POST-006", "교육자료 단건 조회 성공"),
    OK_CREATE_TAG(HttpStatus.OK, "POST-009", "태그 생성 성공"),
    OK_SEARCH_TAG(HttpStatus.OK, "POST-010", "태그 검색 성공"),
    OK_SEARCH_POST_BY_TAG(HttpStatus.OK, "POST-011", "태그를 이용한 교육자료 검색 성공");

    private final HttpStatus httpStatus;
    private final String responseCode;
    private final String message;

    public HttpStatus getHttpStatus(PostResponseCode responseCode) {
        return responseCode.getHttpStatus();
    }

    public String getResponseCode(PostResponseCode responseCode) {
        return responseCode.responseCode;
    }

    public String getMessage(PostResponseCode responseCode) {
        return responseCode.getMessage();
    }
}
