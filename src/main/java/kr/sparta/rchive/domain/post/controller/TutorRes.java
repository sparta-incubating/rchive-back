package kr.sparta.rchive.domain.post.controller;

import lombok.Builder;

@Builder
public record TutorRes(
        Long tutorId,
        String tutorName
) {
}
