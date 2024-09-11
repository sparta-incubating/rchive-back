package kr.sparta.rchive.domain.user.dto.response;

import java.util.List;
import kr.sparta.rchive.domain.user.dto.TrackNameInfo;
import lombok.Builder;

@Builder
public record RoleGetTrackNameListRes(
        List<TrackNameInfo> trackNameList
) {

}
