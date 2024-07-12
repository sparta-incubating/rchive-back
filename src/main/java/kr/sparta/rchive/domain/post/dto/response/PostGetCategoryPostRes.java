package kr.sparta.rchive.domain.post.dto.response;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record PostGetCategoryPostRes(
        String tutor,
        String title,
        LocalDate uploadedAt,
        List<String> tagNameList
) {
}
