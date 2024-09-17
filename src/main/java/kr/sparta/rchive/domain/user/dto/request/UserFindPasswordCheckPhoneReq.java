package kr.sparta.rchive.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record UserFindPasswordCheckPhoneReq(
        @Schema(description = "이메일", example = "email@email.com")
        @Email(message = "올바르지 않은 이메일 형식")
        String email,

        @Schema(description = "이름", example = "아무개")
        String username,

        @Schema(description = "휴대폰 번호", example = "01012345678")
        @Pattern(regexp = "^[0-9]{11}$", message = "휴대폰번호는 숫자만 11자리")
        String phone
) {

}
