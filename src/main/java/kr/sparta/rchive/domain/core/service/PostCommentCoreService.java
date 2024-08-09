package kr.sparta.rchive.domain.core.service;

import kr.sparta.rchive.domain.comment.dto.request.CommentCreateReq;
import kr.sparta.rchive.domain.comment.entity.Comment;
import kr.sparta.rchive.domain.comment.service.CommentService;
import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.exception.PostCustomException;
import kr.sparta.rchive.domain.post.exception.PostExceptionCode;
import kr.sparta.rchive.domain.post.service.PostService;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.service.TrackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostCommentCoreService {

    private final CommentService commentService;
    private final PostService postService;
    private final TrackService trackService;

    @Transactional
    public void createComment(User user, Long postId, Long parentCommentId, CommentCreateReq request) {
        Post findPost = postService.findPostById(postId);

        Comment findComment = null;

        if(parentCommentId != null) {
            findComment = commentService.findCommentByCommentId(parentCommentId);

            if(findComment.getIsDeleted()) {
                throw new PostCustomException(PostExceptionCode.NOT_FOUND_COMMENT);
            }
        }

        commentService.createComment(user, findPost, findComment, request);
    }
}
