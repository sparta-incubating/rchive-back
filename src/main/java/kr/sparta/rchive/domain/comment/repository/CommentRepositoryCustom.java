package kr.sparta.rchive.domain.comment.repository;

import kr.sparta.rchive.domain.comment.entity.Comment;

import java.util.List;

public interface CommentRepositoryCustom {
    List<Comment> findParentCommentListNotDeletedByPostId(Long postId);
}
