package kr.sparta.rchive.domain.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.sparta.rchive.domain.post.entity.*;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Post> findPostListInBackOfficePostTypeAllByPm(LocalDate startDate, LocalDate endDate, Boolean isOpened, Integer searchPeriod, TrackNameEnum trackName) {

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
                        searchPeriod != null ? post.track.period.eq(searchPeriod) : null,
                        post.track.trackName.eq(trackName)
                )
                .orderBy(post.uploadedAt.desc())
                .fetch();
    }

    @Override
    public List<Post> findPostListInBackOfficePostTypeAllByApm(LocalDate startDate, LocalDate endDate, Boolean isOpened, Long trackId) {

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
                        post.track.id.eq(trackId)
                )
                .orderBy(post.uploadedAt.desc())
                .fetch();
    }

    @Override
    public List<Post> findPostListInBackOfficePostTypeNotNullByPM(PostTypeEnum postType, LocalDate startDate,
                                                                  LocalDate endDate, Integer searchPeriod, Boolean isOpened, TrackNameEnum trackName) {

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
                        isOpened != null ? post.isOpened.eq(isOpened) : null,
                        post.track.trackName.eq(trackName)
                )
                .orderBy(post.uploadedAt.desc())
                .fetch();
    }

    @Override
    public List<Post> findPostListInBackOfficePostTypeNotNullApm(PostTypeEnum postType, LocalDate startDate,
                                                                 LocalDate endDate, Long trackId, Boolean isOpened) {

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
                        post.track.id.eq(trackId),
                        isOpened != null ? post.isOpened.eq(isOpened) : null
                )
                .orderBy(post.uploadedAt.desc())
                .fetch();
    }

    @Override
    public List<Post> findAllByPostTypeAndTrackIdUserRoleUser(PostTypeEnum postType, Long trackId) {

        QPost post = QPost.post;
        QPostTag postTag = QPostTag.postTag;
        QTag tag = QTag.tag;

        return queryFactory
                .select(post).distinct()
                .from(post)
                .leftJoin(post.postTagList, postTag).fetchJoin()
                .leftJoin(postTag.tag, tag).fetchJoin()
                .where(
                        post.postType.eq(postType),
                        post.track.id.eq(trackId),
                        post.isOpened.eq(true)
                )
                .orderBy(post.uploadedAt.desc())
                .fetch();
    }

    @Override
    public List<Post> findAllByPostTypeAndTrackIdUserRoleManager(PostTypeEnum postType, Long trackId) {

        QPost post = QPost.post;
        QPostTag postTag = QPostTag.postTag;
        QTag tag = QTag.tag;

        return queryFactory
                .select(post).distinct()
                .from(post)
                .leftJoin(post.postTagList, postTag).fetchJoin()
                .leftJoin(postTag.tag, tag).fetchJoin()
                .where(
                        post.postType.eq(postType),
                        post.track.id.eq(trackId)
                )
                .orderBy(post.uploadedAt.desc())
                .fetch();
    }

    @Override
    public List<Post> findPostListByTagIdAndTrackIdWithTagList(Long tagId, Long trackId) {

        QPost post = QPost.post;
        QPostTag postTag = QPostTag.postTag;
        QTag tag = QTag.tag;

        return queryFactory.selectDistinct(post)
                .from(post)
                .join(post.postTagList, postTag).fetchJoin()
                .join(postTag.tag, tag).fetchJoin()
                .where(
                        post.id.in(
                                queryFactory.select(post.id)
                                        .from(post)
                                        .join(post.postTagList, postTag)
                                        .where(postTag.tag.id.eq(tagId))
                        ),
                        post.track.id.eq(trackId))
                .fetch();
    }
}
