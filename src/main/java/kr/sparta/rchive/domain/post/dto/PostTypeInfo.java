package kr.sparta.rchive.domain.post.dto;

import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import lombok.Builder;

@Builder
public record PostTypeInfo(
        String key,
        String value
) {
    public static PostTypeInfo of (PostTypeEnum postTypeEnum) {
        return PostTypeInfo.builder()
                .key(postTypeEnum.name())
                .value(postTypeEnum.getName())
                .build();
    }
}
