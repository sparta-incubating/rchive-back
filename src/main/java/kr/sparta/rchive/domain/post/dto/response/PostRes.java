package kr.sparta.rchive.domain.post.dto.response;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record PostRes(
        String title,
        String tutor,
        LocalDate uploadedAt
) {

}
