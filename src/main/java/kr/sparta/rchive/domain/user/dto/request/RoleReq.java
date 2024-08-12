package kr.sparta.rchive.domain.user.dto.request;

import kr.sparta.rchive.domain.user.enums.TrackNameEnum;

public record RoleReq(
        TrackNameEnum trackName,
        Integer period
) {

}
