package kr.sparta.rchive.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalResponseCode implements ResponseCode {

    /* 200 OK */
    OK(HttpStatus.OK, "REQUEST SUCCESS, OK"),

    /* 201 CREATED */
    CREATE(HttpStatus.CREATED, "REQUEST SUCCESS, CREATE"),

    /* 204 NO CONTENT */
    NO_CONTENT(HttpStatus.NO_CONTENT, "REQUEST SUCCESS, NO CONTENT")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public HttpStatus getHttpStatus(GlobalResponseCode responseCode){
        return responseCode.getHttpStatus();
    }

    public String getMessage(GlobalResponseCode responseCode){
        return responseCode.getMessage();
    }
}
