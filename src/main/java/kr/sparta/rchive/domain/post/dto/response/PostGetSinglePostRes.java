package kr.sparta.rchive.domain.post.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.sparta.rchive.domain.post.dto.TagInfo;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PostGetSinglePostRes(
        Long postId,
        String title,
        String tutor,
        String videoLink,
        String contentLink,
        List<TagInfo> tagList,
        Boolean isBookmarked
) {
}
