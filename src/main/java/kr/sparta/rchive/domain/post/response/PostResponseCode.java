package kr.sparta.rchive.domain.post.response;

import kr.sparta.rchive.global.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PostResponseCode implements ResponseCode {

    // OK 200
    OK_SEARCH_EDUCATION_DATA(HttpStatus.OK, "SEARCH EDUCATION DATA"),
    OK_SEARCH_TAG(HttpStatus.OK, "SEARCH TAG"),
    OK_CREATE_TAG(HttpStatus.OK, "CREATE TAG");

    private final HttpStatus httpStatus;
    private final String message;

    public HttpStatus getHttpStatus(PostResponseCode responseCode) {
        return responseCode.getHttpStatus();
    }

    public String getMessage(PostResponseCode responseCode) {
        return responseCode.getMessage();
    }
}
