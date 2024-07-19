package kr.sparta.rchive.domain.user.dto.response;

import java.time.LocalDate;
import kr.sparta.rchive.domain.user.enums.AuthEnum;
import kr.sparta.rchive.domain.user.enums.TrackRoleEnum;
import lombok.Builder;

@Builder
public record RoleGetTrackRoleRequestListRes(
    String username,
    TrackRoleEnum trackRole,
    Integer period,
    String email,
    LocalDate createdAt,
    AuthEnum auth
) {

}
