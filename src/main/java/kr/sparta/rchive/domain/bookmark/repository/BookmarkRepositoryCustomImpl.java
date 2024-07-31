package kr.sparta.rchive.domain.bookmark.repository;

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
}
