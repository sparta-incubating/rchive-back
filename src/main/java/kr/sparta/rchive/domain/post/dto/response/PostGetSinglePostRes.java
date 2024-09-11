package kr.sparta.rchive.domain.post.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.sparta.rchive.domain.post.dto.PostTypeInfo;
import kr.sparta.rchive.domain.post.dto.TagInfo;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PostGetSinglePostRes(
        Long postId,
        String title,
        PostTypeInfo postType,
        String tutor,
        String videoLink,
        String contentLink,
        LocalDate uploadedAt,
        List<TagInfo> tagList,
        Boolean isBookmarked
) {
}
