package kr.sparta.rchive.domain.comment.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.sparta.rchive.domain.comment.dto.CommentRes;
import kr.sparta.rchive.domain.comment.dto.response.CommentGetRes;
import kr.sparta.rchive.domain.comment.entity.QComment;
import kr.sparta.rchive.domain.post.entity.QPost;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentRes> findParentCommentListByPostId(Long postId) {

        QComment comment = QComment.comment;
        QPost post = QPost.post;
        QComment childComment = new QComment("childComment");

        BooleanExpression hasChild = queryFactory
            .selectOne()
            .from(childComment)
            .where(
                childComment.parentComment.id.eq(comment.id)
            ).exists();

        return queryFactory
            .select(Projections.constructor(
                CommentRes.class,
                comment,
                hasChild.as("hasChild")
            ))
            .from(comment)
            .join(comment.post, post)
            .where(
                post.id.eq(postId),
                comment.parentComment.isNull()
            )
            .orderBy(comment.createdAt.asc())
            .fetch();
    }
}
