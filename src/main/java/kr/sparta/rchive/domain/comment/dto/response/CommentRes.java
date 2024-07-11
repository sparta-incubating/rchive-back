package kr.sparta.rchive.domain.comment.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentRes(
        Long id,
        String content,
        LocalDateTime createdAt
) {
}
