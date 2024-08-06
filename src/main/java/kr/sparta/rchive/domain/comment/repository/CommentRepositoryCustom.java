package kr.sparta.rchive.domain.comment.repository;

import kr.sparta.rchive.domain.comment.dto.CommentRes;
import kr.sparta.rchive.domain.comment.dto.response.CommentGetRes;

import java.util.List;

public interface CommentRepositoryCustom {
    List<CommentRes> findParentCommentListByPostId(Long postId);
}
