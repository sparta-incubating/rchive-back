package kr.sparta.rchive.domain.post.dto.response;

import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import lombok.Builder;

@Builder
public record PostGetPostTypeRes(
        PostTypeEnum postTypeEnum,
        String postType
) {
}
