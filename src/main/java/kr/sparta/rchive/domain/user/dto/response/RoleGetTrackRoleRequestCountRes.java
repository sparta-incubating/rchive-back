package kr.sparta.rchive.domain.user.dto.response;

import lombok.Builder;

@Builder
public record RoleGetTrackRoleRequestCountRes(
        int statusAll,
        int statusWait,
        int statusApprove
) {

}
