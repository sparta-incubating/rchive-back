package kr.sparta.rchive.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record CommentCreateReq(
        @Schema(description = "댓글 내용", example = "댓글")
        String content
) {
}
