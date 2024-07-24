package kr.sparta.rchive.global.s3.response;

import kr.sparta.rchive.global.response.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum S3ResponseCode implements ResponseCode {

    // OK 200
    OK_FILE_UPLOAD(HttpStatus.OK,"GLOBAL-004", "UPLOAD FILE SUCCESS, OK"),
    OK_FILE_DELETE(HttpStatus.OK,"GLOBAL-005", "DELETE FILE SUCCESS, OK");

    private final HttpStatus httpStatus;
    private final String responseCode;
    private final String message;

    public HttpStatus getHttpStatus(S3ResponseCode responseCode) {
        return responseCode.getHttpStatus();
    }

    public String getResponseCode(S3ResponseCode responseCode) {
        return responseCode.responseCode;
    }

    public String getMessage(S3ResponseCode responseCode) {
        return responseCode.getMessage();
    }
}
