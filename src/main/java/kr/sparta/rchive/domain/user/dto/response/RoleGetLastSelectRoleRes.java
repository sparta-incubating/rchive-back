package kr.sparta.rchive.domain.user.dto.response;

import kr.sparta.rchive.domain.user.dto.TrackNameInfo;
import kr.sparta.rchive.domain.user.enums.TrackRoleEnum;
import lombok.Builder;

@Builder
public record RoleGetLastSelectRoleRes(
        Long trackId,
        TrackRoleEnum trackRole,
        TrackNameInfo trackName,
        int period
) {

}
