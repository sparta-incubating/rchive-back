package kr.sparta.rchive.domain.comment.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentGetRes(
        Long id,
        String content,
        String username,
        String email,
        String nickname,
        Boolean hasChild,
        LocalDateTime createdAt
) {
}
