package kr.sparta.rchive.domain.user.dto;

import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import lombok.Builder;

@Builder
public record TrackNameInfo(
        String key,
        String value
) {

    public static TrackNameInfo of(TrackNameEnum trackNameEnum) {
        return TrackNameInfo.builder()
                .key(trackNameEnum.name())
                .value(trackNameEnum.getValue())
                .build();
    }
}
