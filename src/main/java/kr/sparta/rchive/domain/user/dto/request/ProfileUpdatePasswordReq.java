package kr.sparta.rchive.domain.user.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record ProfileUpdatePasswordReq(
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{6,20}$", message = "비밀번호는 6~20자 영문자와 숫자를 포함해야 합니다")
        String originPassword,
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{6,20}$", message = "비밀번호는 6~20자 영문자와 숫자를 포함해야 합니다")
        String newPassword
) {

}
