package kr.sparta.rchive.domain.comment.repository;

import java.util.List;
import kr.sparta.rchive.domain.comment.dto.CommentUserInfo;

public interface CommentRepositoryCustom {
    List<CommentUserInfo> findParentCommentListByPostId(Long postId);
}
