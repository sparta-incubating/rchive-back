package kr.sparta.rchive.domain.comment.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentProfileRes(
        Long commentId,
        Long postId,
        String content,
        LocalDateTime createdAt
) {
}
