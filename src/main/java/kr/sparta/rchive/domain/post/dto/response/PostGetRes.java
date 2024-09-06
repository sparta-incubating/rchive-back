package kr.sparta.rchive.domain.post.dto.response;

import kr.sparta.rchive.domain.post.dto.TagInfo;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record PostGetRes(
        Long postId,
        String thumbnailUrl,
        PostTypeEnum postType,
        String tutor,
        String title,
        LocalDate uploadedAt,
        List<TagInfo> tagList,
        Boolean isBookmarked
) {
}
