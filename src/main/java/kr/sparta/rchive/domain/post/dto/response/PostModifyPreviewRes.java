package kr.sparta.rchive.domain.post.dto.response;

import kr.sparta.rchive.domain.post.dto.PostTypeInfo;
import kr.sparta.rchive.domain.post.dto.TagInfo;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record PostModifyPreviewRes(
        String thumbnailUrl,
        String title,
        String contentLink,
        String videoLink,
        Integer period,
        PostTypeInfo postType,
        TutorRes tutorRes,
        List<String> tagNameList,
        LocalDate uploadedAt,
        Boolean isOpened
) {
}
