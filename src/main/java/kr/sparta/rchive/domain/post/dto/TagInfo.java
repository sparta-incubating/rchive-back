package kr.sparta.rchive.domain.post.dto;

import lombok.Builder;

@Builder
public record TagInfo(
    Long tagId,
    String tagName
) {

}
