package kr.sparta.rchive.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.domain.user.enums.TrackRoleEnum;
import lombok.Builder;

@Builder
public record RoleRequestListReq(
        @Schema(description = "트랙명", example = "ANDROID")
        TrackNameEnum trackName,

        @Schema(description = "기수", example = "1")
        int period,

        @Schema(description = "트랙 권한", example = "STUDENT")
        TrackRoleEnum trackRole,

        @Schema(description = "이메일", example = "email@email.com")
        String email
) {

}
