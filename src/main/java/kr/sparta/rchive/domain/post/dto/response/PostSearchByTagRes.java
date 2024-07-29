package kr.sparta.rchive.domain.post.dto.response;

import kr.sparta.rchive.domain.post.dto.TagInfo;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record PostSearchByTagRes(
        Long postId,
        String thumbnailUrl,
        String title,
        String tutor,
        LocalDate uploadedAt,
        PostTypeEnum postType,
        List<TagInfo> tagList
        // 제목 카테고리 튜터 기수 공개여부 날짜 태그
) {

}
