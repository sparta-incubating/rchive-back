package kr.sparta.rchive.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record ProfileUpdateProfileImgReq(
        @Schema(description = "프로필 이미지", example = "default")
        String profileImg
) {

}
