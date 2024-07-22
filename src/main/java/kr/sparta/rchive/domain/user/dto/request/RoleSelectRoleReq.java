package kr.sparta.rchive.domain.user.dto.request;

import kr.sparta.rchive.domain.user.enums.TrackNameEnum;

public record RoleSelectRoleReq(
        TrackNameEnum trackName,
        Integer period
) {

}
