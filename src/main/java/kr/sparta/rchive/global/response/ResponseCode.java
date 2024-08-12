package kr.sparta.rchive.global.response;

import org.springframework.http.HttpStatus;

public interface ResponseCode {

    public HttpStatus getHttpStatus();
    public String getResponseCode();
    public String getMessage();

}
