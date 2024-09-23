package kr.sparta.rchive.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.Builder;

@Builder
public record UserFindPasswordCheckEmailReq(
        @Schema(description = "이메일", example = "email@email.com")
        @Email(message = "올바르지 않은 이메일 형식")
        String email,

        @Schema(description = "이름", example = "아무개")
        String username
) {

}
