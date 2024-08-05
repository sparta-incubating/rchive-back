package kr.sparta.rchive.domain.comment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.sparta.rchive.domain.comment.entity.Comment;
import kr.sparta.rchive.domain.comment.entity.QComment;
import kr.sparta.rchive.domain.post.entity.QPost;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> findParentCommentListNotDeletedByPostId(Long postId) {

        QComment comment = QComment.comment;
        QPost post = QPost.post;

        return queryFactory
                .select(comment).distinct()
                .from(comment)
                .leftJoin(comment.post, post).fetchJoin()
                .where(
                        post.id.eq(postId),
                        comment.isDeleted.eq(false),
                        comment.parentComment.isNull()
                )
                .fetch();
    }
}
