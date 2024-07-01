package kr.sparta.rchive.domain.post.dto.response;

import java.time.LocalDate;
import java.util.List;
import kr.sparta.rchive.domain.post.entity.Tag;
import lombok.Builder;

@Builder
public record PostSearchByTagRes(
        String title,
        String tutor,
        LocalDate uploadedAt,
        List<Tag> tagList
) {

}
