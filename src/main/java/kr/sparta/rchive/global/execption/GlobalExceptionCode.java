package kr.sparta.rchive.global.execption;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum GlobalExceptionCode implements ExceptionCode {

    /*  400 BAD_REQUEST : 잘못된 요청  */
    BAD_REQUEST_INVALID_VALUE(HttpStatus.BAD_REQUEST, "GLOBAL-001", "유효하지 않은 값"),
    BAD_REQUEST_INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "GLOBAL-002", "파라미터 누락"),
    BAD_REQUEST_IMAGE_EXTENSION_NOT_EXIST(HttpStatus.BAD_REQUEST, "GLOBAL-010",
            "이미지의 확장자가 존재하지 않음"),
    BAD_REQUEST_IMAGE_EXTENSION_MISMATCH(HttpStatus.BAD_REQUEST, "GLOBAL-011", "확장자가 옳바르지 않음"),
    BAD_REQUEST_IMAGE_NOT_UPLOAD(HttpStatus.BAD_REQUEST, "GLOBAL-011", "업로드한 이미지가 존재하지 않음"),

    /*  401 UNAUTHORIZED : 인증 안됨  */

    /*  403 FORBIDDEN : 권한 없음  */
    FORBIDDEN_DENIED_AUTHORITY(HttpStatus.FORBIDDEN, "GLOBAL-003", "권한이 없음"),

    /*  404 NOT_FOUND : Resource 권한 없음, Resource 를 찾을 수 없음  */
    NOT_FOUND_ACCESS_DENIED(HttpStatus.NOT_FOUND, "GLOBAL-004", "접근 권한이 없음"),

    /*  409 CONFLICT : Resource 중복  */

    /*  500 INTERNAL_SERVER_ERROR : 서버 에러  */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GLOBAL-005", "내부 서버 에러"),
    INTERNAL_SERVER_ERROR_IMAGE_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "GLOBAL-009",
            "이미지 업로드 실패"),
    INTERNAL_SERVER_ERROR_IMAGE_DELETE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "GLOBAL-012",
            "이미지 삭제 실패");

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
    public String getExplainError() throws NoSuchFieldException {
        Field field = this.getClass().getField(this.name());
        ExplainError annotation = field.getAnnotation(ExplainError.class);
        return Objects.nonNull(annotation) ? annotation.value() : this.getReason();
    }
}
