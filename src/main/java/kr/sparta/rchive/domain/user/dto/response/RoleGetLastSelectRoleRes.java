package kr.sparta.rchive.domain.user.dto.response;

import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.domain.user.enums.TrackRoleEnum;
import lombok.Builder;

@Builder
public record RoleGetLastSelectRoleRes(
        TrackRoleEnum trackRole,
        TrackNameEnum trackName,
        int period
) {

}
