package kr.sparta.rchive.global.s3.exception.statement;

import kr.sparta.rchive.global.execption.ExceptionCode;
import kr.sparta.rchive.global.execption.ExceptionReason;
import kr.sparta.rchive.global.execption.ExplainException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.util.Objects;

@AllArgsConstructor
@Getter
public enum UploadThumbnailException implements ExceptionCode {
    BAD_REQUEST_IMAGE_EXTENSION_NOT_EXIST(HttpStatus.BAD_REQUEST,"S3-0001","이미지의 확장자가 존재하지 않음"),
    BAD_REQUEST_IMAGE_EXTENSION_MISMATCH(HttpStatus.BAD_REQUEST, "S3-0002","확장자가 올바르지 않음"),
    INTERNAL_SERVER_ERROR_IMAGE_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR,"S3-5001", "이미지 업로드 실패")
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

    @Override
    public ExceptionReason getExceptionReason() {
        return ExceptionReason.builder()
                .errorCode(errorCode)
                .httpStatus(httpStatus)
                .message(message).build();
    }

    @Override
    public String getExplainException() throws NoSuchFieldException {
        Field field = this.getClass().getField(this.name());
        ExplainException annotation = field.getAnnotation(ExplainException.class);
        return Objects.nonNull(annotation) ? annotation.value() : this.getMessage();
    }
}