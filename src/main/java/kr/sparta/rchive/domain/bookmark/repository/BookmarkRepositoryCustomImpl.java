package kr.sparta.rchive.domain.bookmark.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.sparta.rchive.domain.bookmark.entity.Bookmark;
import kr.sparta.rchive.domain.bookmark.entity.QBookmark;
import kr.sparta.rchive.domain.post.entity.QPost;
import kr.sparta.rchive.domain.post.entity.QPostTag;
import kr.sparta.rchive.domain.post.entity.QTag;
import kr.sparta.rchive.domain.post.entity.QTutor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BookmarkRepositoryCustomImpl implements BookmarkRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Bookmark> findBookmarkListByUserId(Long userId) {
        QBookmark bookmark = QBookmark.bookmark;
        QPost post = QPost.post;
        QPostTag postTag = QPostTag.postTag;
        QTag tag = QTag.tag;
        QTutor tutor = QTutor.tutor;

        return queryFactory
                .select(bookmark)
                .from(bookmark)
                .leftJoin(bookmark.post, post).fetchJoin()
                .leftJoin(post.postTagList, postTag).fetchJoin()
                .leftJoin(postTag.tag, tag).fetchJoin()
                .leftJoin(post.tutor, tutor).fetchJoin()
                .where(bookmark.user.id.eq(userId))
                .fetch();
    }

    @Override
    public List<Bookmark> findBookmarkListByUserIdAndKeyword(Long userId, String keyword) {
        QBookmark bookmark = QBookmark.bookmark;
        QPost post = QPost.post;
        QPostTag postTag = QPostTag.postTag;
        QTag tag = QTag.tag;
        QTutor tutor = QTutor.tutor;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(bookmark.user.id.eq(userId));
        builder.and(
                Expressions.stringTemplate("function('replace', {0}, {1}, {2})", post.title, " ", "")
                        .toLowerCase().contains(keyword.toLowerCase())
                        .or(post.content.toLowerCase().contains(keyword.toLowerCase()))
        );

        return queryFactory
                .select(bookmark)
                .from(bookmark)
                .leftJoin(bookmark.post, post).fetchJoin()
                .leftJoin(post.postTagList, postTag).fetchJoin()
                .leftJoin(postTag.tag, tag).fetchJoin()
                .leftJoin(post.tutor, tutor).fetchJoin()
                .where(builder)
                .fetch();
    }
}
