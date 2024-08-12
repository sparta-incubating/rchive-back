package kr.sparta.rchive.domain.post.dto.response;

import java.time.LocalDate;
import java.util.List;

import kr.sparta.rchive.domain.post.dto.TagInfo;
import lombok.Builder;

@Builder
public record PostRes(
        Long postId,
        String title,
        String tutor,
        LocalDate uploadedAt,
        List<TagInfo> tagInfoList
) {

}
