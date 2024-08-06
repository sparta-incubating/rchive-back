package kr.sparta.rchive.domain.comment.dto;

import kr.sparta.rchive.domain.comment.entity.Comment;
import lombok.Builder;

@Builder
public record CommentRes(
    Comment comment,
    Boolean hasChild
) {

}
