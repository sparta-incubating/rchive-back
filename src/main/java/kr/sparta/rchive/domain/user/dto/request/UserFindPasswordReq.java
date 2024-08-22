package kr.sparta.rchive.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record UserFindPasswordReq(
        @Email(message = "올바르지 않은 이메일 형식")
        String email,
        String username,
        @Pattern(regexp = "^[0-9]{11}$", message = "휴대폰번호는 숫자만 11자리")
        String phone
) {

}
