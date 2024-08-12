package kr.sparta.rchive.domain.user.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record RoleGetTrackPeriodListRes(
        List<Integer> trackPeriodList
) {

}
