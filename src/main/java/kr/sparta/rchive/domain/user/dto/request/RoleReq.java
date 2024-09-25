package kr.sparta.rchive.domain.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;

public record RoleReq(
        @Schema(description = "트랙명", example = "ANDROID")
        TrackNameEnum trackName,

        @Schema(description = "기수", example = "1")
        Integer period
) {

}
