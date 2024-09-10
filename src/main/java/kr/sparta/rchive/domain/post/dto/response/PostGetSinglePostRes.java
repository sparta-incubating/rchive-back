package kr.sparta.rchive.domain.post.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.sparta.rchive.domain.post.dto.TagInfo;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PostGetSinglePostRes(
        Long postId,
        String title,
        PostTypeEnum postType,
        String tutor,
        String videoLink,
        String contentLink,
        LocalDate uploadedAt,
        List<TagInfo> tagList,
        Boolean isBookmarked
) {
}
