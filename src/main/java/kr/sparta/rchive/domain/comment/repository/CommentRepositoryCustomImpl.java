package kr.sparta.rchive.domain.comment.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import kr.sparta.rchive.domain.comment.dto.CommentUserInfo;
import kr.sparta.rchive.domain.comment.entity.QComment;
import kr.sparta.rchive.domain.post.entity.QPost;
import kr.sparta.rchive.domain.user.entity.QUser;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentUserInfo> findParentCommentListByPostId(Long postId) {

        QComment comment = QComment.comment;
        QPost post = QPost.post;
        QUser user = QUser.user;
        QComment childComment = new QComment("childComment");

        BooleanExpression hasChild = queryFactory
            .selectOne()
            .from(childComment)
            .where(
                childComment.parentComment.id.eq(comment.id)
            ).exists();

        return queryFactory
            .select(Projections.constructor(
                CommentUserInfo.class,
                comment,
                user,
                hasChild.as("hasChild")
            ))
            .from(comment)
            .join(comment.post, post)
            .join(comment.user, user)
            .where(
                post.id.eq(postId),
                comment.parentComment.isNull()
            )
            .orderBy(comment.createdAt.asc())
            .fetch();
    }
}
