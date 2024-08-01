package kr.sparta.rchive.global.s3.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum S3ExceptionCode {
    /*  400 BAD_REQUEST : 잘못된 요청  */
    BAD_REQUEST_IMAGE_EXTENSION_NOT_EXIST(HttpStatus.BAD_REQUEST,"S3-0001","이미지의 확장자가 존재하지 않음"),
    BAD_REQUEST_IMAGE_EXTENSION_MISMATCH(HttpStatus.BAD_REQUEST, "S3-0002","확장자가 올바르지 않음"),
    BAD_REQUEST_IMAGE_NOT_UPLOAD(HttpStatus.BAD_REQUEST, "S3-0003","업로드한 이미지가 존재하지 않음"),
    /*  401 UNAUTHORIZED : 인증 안됨  */

    /*  403 FORBIDDEN : 권한 없음  */

    /*  404 NOT_FOUND : Resource 권한 없음, Resource 를 찾을 수 없음  */

    /*  409 CONFLICT : Resource 중복  */

    /*  500 INTERNAL_SERVER_ERROR : 서버 에러  */
    INTERNAL_SERVER_ERROR_IMAGE_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR,"S3-5001", "이미지 업로드 실패"),
    INTERNAL_SERVER_ERROR_IMAGE_DELETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR,"S3-5002", "이미지 삭제 실패");
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
}
