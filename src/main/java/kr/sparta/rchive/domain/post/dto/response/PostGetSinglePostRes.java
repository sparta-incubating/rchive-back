package kr.sparta.rchive.domain.post.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.sparta.rchive.domain.post.dto.TagInfo;
import lombok.Builder;

import java.util.List;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PostGetSinglePostRes(
    String title,
    String tutor,
    String videoLink,
    String detail,
    List<TagInfo> tagList
//    List<CommentRes> commentResList  TODO: 추후에 댓글 구현하며 추가할 예정
) {
}
