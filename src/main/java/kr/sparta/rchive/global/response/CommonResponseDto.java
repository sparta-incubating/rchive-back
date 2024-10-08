package kr.sparta.rchive.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import kr.sparta.rchive.global.execption.CustomException;
import kr.sparta.rchive.global.execption.GlobalExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponseDto<T> {

    private int status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String responseCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorCode;

    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public CommonResponseDto (int status, String message, T data){
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public CommonResponseDto(int value, String exceptionCode, String message) {
        this.status = value;
        this.errorCode = exceptionCode;
        this.message = message;
    }

    public static <T> CommonResponseDto<T> of(ResponseCode responseCode, T data) {
        return new CommonResponseDto<T>(
                responseCode.getHttpStatus().value(),
                responseCode.getMessage(),
                data
        );
    }

    public static <T> CommonResponseDto<T> of(GlobalExceptionCode responseCode) {
        return new CommonResponseDto<T>(
                responseCode.getHttpStatus().value(),
                responseCode.getErrorCode(),
                responseCode.getMessage()
        );
    }

    public static <T> CommonResponseDto<T> of(CustomException responseCode) {
        return new CommonResponseDto<T>(
                responseCode.getHttpStatus().value(),
                responseCode.getErrorCode(),
                responseCode.getMessage()
        );
    }

}
