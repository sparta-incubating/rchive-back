package kr.sparta.rchive.domain.post.dto.response;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record PostSearchEducationDataRes(
        String title,
        String tutor,
        LocalDate uploadedAt,
        List<String> tagList
) {
}
