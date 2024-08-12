package kr.sparta.rchive.domain.post.dto.response;

import lombok.Builder;

@Builder
public record PostModifyRes(
        Long postId
) {

}
