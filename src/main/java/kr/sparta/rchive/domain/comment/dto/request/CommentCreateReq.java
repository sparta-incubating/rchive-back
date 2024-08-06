package kr.sparta.rchive.domain.comment.dto.request;

import lombok.Builder;

@Builder
public record CommentCreateReq(
    String content
) {
}
