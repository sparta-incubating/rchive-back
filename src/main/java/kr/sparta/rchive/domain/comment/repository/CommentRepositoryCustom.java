package kr.sparta.rchive.domain.comment.repository;

import java.util.List;
import kr.sparta.rchive.domain.comment.dto.CommentUserInfo;
import kr.sparta.rchive.domain.comment.entity.Comment;

public interface CommentRepositoryCustom {
    List<CommentUserInfo> findParentCommentListByPostId(Long postId);

    List<Comment> findCommentByUserId(Long id);
}
