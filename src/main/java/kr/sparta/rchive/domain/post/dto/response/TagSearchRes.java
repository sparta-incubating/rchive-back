package kr.sparta.rchive.domain.post.dto.response;

import lombok.Builder;

@Builder
public record TagSearchRes(
        Long tagId,
        String tagName
) {
}
