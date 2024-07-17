package kr.sparta.rchive.domain.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.entity.QPost;
import kr.sparta.rchive.domain.post.entity.QPostTag;
import kr.sparta.rchive.domain.post.entity.QTag;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> findPostListInBackOfficePostTypeAllByPm(LocalDate startDate, LocalDate endDate, Boolean isOpened, Integer searchPeriod) {

        QPost post = QPost.post;
        QPostTag postTag = QPostTag.postTag;
        QTag tag = QTag.tag;

        return queryFactory
            .select(post).distinct()
            .from(post)
            .leftJoin(post.postTagList, postTag).fetchJoin()
            .leftJoin(postTag.tag, tag).fetchJoin()
            .where(
                startDate != null ? post.uploadedAt.between(startDate, endDate) : null,
                isOpened != null ? post.isOpened.eq(isOpened) : null,
                searchPeriod != null ? post.track.period.eq(searchPeriod) : null
            )
            .fetch();
    }

    @Override
    public List<Post> findPostListInBackOfficePostTypeAllByApm(LocalDate startDate, LocalDate endDate, Boolean isOpened, Integer period) {
        QPost post = QPost.post;
        QPostTag postTag = QPostTag.postTag;
        QTag tag = QTag.tag;

        return queryFactory
            .select(post).distinct()
            .from(post)
            .leftJoin(post.postTagList, postTag).fetchJoin()
            .leftJoin(postTag.tag, tag).fetchJoin()
            .where(
                startDate != null ? post.uploadedAt.between(startDate, endDate) : null,
                isOpened != null ? post.isOpened.eq(isOpened) : null,
                post.track.period.eq(period)
            )
            .fetch();
    }

    @Override
    public List<Post> findPostListInBackOfficePostTypeNotNullByPM(PostTypeEnum postType, LocalDate startDate,
        LocalDate endDate, Integer searchPeriod, Boolean isOpened) {
        QPost post = QPost.post;
        QPostTag postTag = QPostTag.postTag;
        QTag tag = QTag.tag;

        return queryFactory
            .select(post).distinct()
            .from(post)
            .leftJoin(post.postTagList, postTag).fetchJoin()
            .leftJoin(postTag.tag, tag).fetchJoin()
            .where(
                postType != null ? post.postType.eq(postType) : null,
                startDate != null ? post.uploadedAt.between(startDate, endDate) : null,
                searchPeriod != null ? post.track.period.eq(searchPeriod) : null,
                isOpened != null ? post.isOpened.eq(isOpened) : null
            )
            .fetch();
    }

    @Override
    public List<Post> findPostListInBackOfficePostTypeNotNullApm(PostTypeEnum postType, LocalDate startDate,
                                                                 LocalDate endDate, Integer period, Boolean isOpened) {
        QPost post = QPost.post;
        QPostTag postTag = QPostTag.postTag;
        QTag tag = QTag.tag;

        return queryFactory
            .select(post).distinct()
            .from(post)
            .leftJoin(post.postTagList, postTag).fetchJoin()
            .leftJoin(postTag.tag, tag).fetchJoin()
            .where(
                postType != null ? post.postType.eq(postType) : null,
                startDate != null ? post.uploadedAt.between(startDate, endDate) : null,
                post.track.period.eq(period),
                isOpened != null ? post.isOpened.eq(isOpened) : null
            )
            .fetch();
    }
}
