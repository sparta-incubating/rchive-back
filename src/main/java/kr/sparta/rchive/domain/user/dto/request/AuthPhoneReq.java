package kr.sparta.rchive.domain.user.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Builder
public record AuthPhoneReq(
        String username,
        @Pattern(regexp = "^[0-9]{11}$", message = "휴대폰번호는 숫자만 11자리")
        String phone
) {

}
