package kr.sparta.rchive.domain.comment.service;

import java.util.stream.Collectors;
import kr.sparta.rchive.domain.comment.dto.request.CommentCreateReq;
import kr.sparta.rchive.domain.comment.dto.response.CommentGetRes;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;

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

        if (user.getUserRole() == UserRoleEnum.USER) {
            if (!Objects.equals(findComment.getUser().getId(), user.getId())) {
                throw new RoleCustomException(RoleExceptionCode.FORBIDDEN_ROLE);
            }
        }

        findComment.delete();

        commentRepository.save(findComment);
    }

    public List<CommentGetRes> getParentCommentList(Long postId) {
        return commentRepository.findParentCommentListByPostId(postId).stream()
            .map(commentRes -> {
                if(commentRes.comment().getIsDeleted()) {
                    return CommentGetRes.builder()
                        .id(null)
                        .content("삭제된 댓글입니다.")
                        .hasChild(commentRes.hasChild())
                        .build();
                }

                return CommentGetRes.builder()
                    .id(commentRes.comment().getId())
                    .content(commentRes.comment().getContent())
                    .createdAt(commentRes.comment().getCreatedAt())
                    .hasChild(commentRes.hasChild())
                    .build();
            }
        ).collect(Collectors.toList());
    }
}
