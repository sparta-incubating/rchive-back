package kr.sparta.rchive.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.domain.user.enums.TrackRoleEnum;
import lombok.Builder;

@Builder
@JsonIgnoreProperties
public record RoleRequestReq (
        TrackNameEnum trackName,
        int period,
        TrackRoleEnum trackRole
) {

}
