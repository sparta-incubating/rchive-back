package kr.sparta.rchive.domain.post.dto.response;

import lombok.Builder;

@Builder
public record TutorRes(
        Long tutorId,
        String tutorName
) {
}
