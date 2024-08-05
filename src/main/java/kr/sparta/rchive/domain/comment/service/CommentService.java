package kr.sparta.rchive.domain.comment.service;

import kr.sparta.rchive.domain.comment.dto.request.CommentCreateReq;
import kr.sparta.rchive.domain.comment.dto.response.CommentRes;
import kr.sparta.rchive.domain.comment.entity.Comment;
import kr.sparta.rchive.domain.comment.repository.CommentRepository;
import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.exception.PostCustomException;
import kr.sparta.rchive.domain.post.exception.PostExceptionCode;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.UserRoleEnum;
import kr.sparta.rchive.domain.user.exception.RoleCustomException;
import kr.sparta.rchive.domain.user.exception.RoleExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;

    public List<CommentRes> findCommentResListByPostId(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(comment -> CommentRes.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    public void createComment(User user, Post post, Comment comment, CommentCreateReq request) {
        Comment createComment = Comment.builder()
                .content(request.content())
                .post(post)
                .user(user)
                .parentComment(comment)
                .build();

        commentRepository.save(createComment);
    }

    public Comment findCommentByCommentId(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new PostCustomException(PostExceptionCode.NOT_FOUND_COMMENT)
        );
    }

    @Transactional
    public void deleteComment(User user, Long commentId) {
        Comment findComment = findCommentByCommentId(commentId);

        if(user.getUserRole() == UserRoleEnum.USER) {
            if(!Objects.equals(findComment.getUser().getId(), user.getId())) {
                throw new RoleCustomException(RoleExceptionCode.FORBIDDEN_ROLE);
            }
        }

        findComment.delete();

        commentRepository.save(findComment);
    }
}
