package kr.sparta.rchive.domain.comment.dto;

import kr.sparta.rchive.domain.comment.entity.Comment;
import kr.sparta.rchive.domain.user.entity.User;
import lombok.Builder;

@Builder
public record CommentRes(
    Comment comment,
    User user,
    Boolean hasChild
) {

}