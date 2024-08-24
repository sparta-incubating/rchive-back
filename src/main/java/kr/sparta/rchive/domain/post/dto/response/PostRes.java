package kr.sparta.rchive.domain.post.dto.response;

import java.time.LocalDate;
import java.util.List;

import kr.sparta.rchive.domain.post.dto.TagInfo;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import lombok.Builder;

@Builder
public record PostRes(
        Long postId,
        String thumbnailUrl,
        PostTypeEnum postType,
        String tutor,
        String title,
        LocalDate uploadedAt,
        List<TagInfo> tagList
) {

}
