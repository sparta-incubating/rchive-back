package kr.sparta.rchive.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import lombok.Builder;

@Builder
public record UserFindPasswordCheckEmailReq(
        @Email(message = "올바르지 않은 이메일 형식")
        String email,
        String username
) {

}
