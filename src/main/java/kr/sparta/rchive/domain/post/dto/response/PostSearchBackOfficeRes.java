package kr.sparta.rchive.domain.post.dto.response;

import kr.sparta.rchive.domain.post.dto.TagInfo;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record PostSearchBackOfficeRes(
        Long postId,
        String thumbnailUrl,
        String title,
        PostTypeEnum postType,
        String contentLink,
        String tutor,
        Integer period,
        Boolean isOpened,
        LocalDate uploadedAt,
        List<TagInfo> tagInfoList
) {
}
