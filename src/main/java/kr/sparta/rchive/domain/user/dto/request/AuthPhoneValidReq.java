package kr.sparta.rchive.domain.user.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record AuthPhoneValidReq(
        String username,
        @Pattern(regexp = "^[0-9]{11}$", message = "휴대폰번호는 숫자만 11자리")
        String phone,
        @Pattern(regexp = "^[0-9]{6}$", message = "인증번호는 숫자만 6자리")
        String authCode
) {

}
