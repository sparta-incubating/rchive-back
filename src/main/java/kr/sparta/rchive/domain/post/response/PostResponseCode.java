package kr.sparta.rchive.domain.post.response;

import kr.sparta.rchive.global.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PostResponseCode implements ResponseCode {

    // OK 200
    OK_SEARCH_POST(HttpStatus.OK, "교육자료 검색 성공"),
    OK_SEARCH_TAG(HttpStatus.OK, "태그 검색 성공"),
    OK_CREATE_TAG(HttpStatus.OK, "태그 생성 성공"),
    OK_SEARCH_POST_BY_TAG(HttpStatus.OK, "태그를 이용한 교육자료 검색 성공"),
    OK_CREATE_POST(HttpStatus.OK, "교육자료 생성 성공");

    private final HttpStatus httpStatus;
    private final String message;

    public HttpStatus getHttpStatus(PostResponseCode responseCode) {
        return responseCode.getHttpStatus();
    }

    public String getMessage(PostResponseCode responseCode) {
        return responseCode.getMessage();
    }
}
