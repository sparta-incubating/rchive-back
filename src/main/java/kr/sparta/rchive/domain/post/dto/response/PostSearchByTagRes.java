package kr.sparta.rchive.domain.post.dto.response;

import java.time.LocalDate;
import java.util.List;
import kr.sparta.rchive.domain.post.entity.Tag;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import lombok.Builder;

@Builder
public record PostSearchByTagRes(
        String title,
        String tutor,
        LocalDate uploadedAt,
        PostTypeEnum postType,
        Integer period,
        Boolean isOpened,
        List<String> tagNameList
        // 제목 카테고리 튜터 기수 공개여부 날짜 태그
) {

}
