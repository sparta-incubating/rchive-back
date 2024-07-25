package kr.sparta.rchive.domain.user.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record ProfileUpdatePhoneReq(
        @Pattern(regexp = "^[0-9]{11}$", message = "휴대폰번호는 숫자만 11자리")
        String phone
) {

}
