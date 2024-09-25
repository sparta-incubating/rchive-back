package kr.sparta.rchive.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record ProfileUpdateNicknameReq(
        @Schema(description = "닉네임", example = "르탄이S2")
        @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$", message = "업데이트 할 닉네임은 특수문자를 제외한 2~10자리")
        String nickname
) {

}
