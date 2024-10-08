package kr.sparta.rchive.domain.post.dto.response;

import kr.sparta.rchive.domain.post.dto.PostTypeInfo;
import kr.sparta.rchive.domain.post.dto.TagInfo;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record PostGetRes(
        Long postId,
        String thumbnailUrl,
        PostTypeInfo postType,
        String tutor,
        String title,
        LocalDate uploadedAt,
        List<TagInfo> tagList,
        Boolean isBookmarked
) {
}
