package kr.sparta.rchive.domain.user.dto.request;

import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.domain.user.enums.TrackRoleEnum;
import lombok.Builder;

@Builder
public record RoleRequestListReq(
        TrackNameEnum trackName,
        int period,
        TrackRoleEnum trackRole,
        String email
) {

}
