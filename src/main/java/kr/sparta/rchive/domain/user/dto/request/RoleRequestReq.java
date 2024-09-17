package kr.sparta.rchive.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.domain.user.enums.TrackRoleEnum;
import lombok.Builder;

@Builder
@JsonIgnoreProperties
public record RoleRequestReq(
        @Schema(description = "트랙명", example = "ANDROID")
        TrackNameEnum trackName,

        @Schema(description = "기수", example = "1")
        int period,

        @Schema(description = "트랙 권한", example = "STUDENT")
        TrackRoleEnum trackRole
) {

}
