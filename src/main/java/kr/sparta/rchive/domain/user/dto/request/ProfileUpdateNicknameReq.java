package kr.sparta.rchive.domain.user.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record ProfileUpdateNicknameReq(
        @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$", message = "업데이트 할 닉네임은 특수문자를 제외한 2~10자리")
        String nickname
) {

}
