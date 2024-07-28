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
    public List<Post> findPostListInBackOfficePostTypeAllByPm(LocalDate startDate, LocalDate endDate, Boolean isOpened,
                                                              Integer searchPeriod, TrackNameEnum trackName, Long tutorId) {

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
                        tutorId != null ? post.tutor.id.eq(tutorId) : null,
                        post.track.trackName.eq(trackName)
                )
                .orderBy(post.uploadedAt.desc())
                .fetch();
    }

    @Override
    public List<Post> findPostListInBackOfficePostTypeAllByApm(LocalDate startDate, LocalDate endDate, Boolean isOpened, Long trackId, Long tutorId) {

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
                        tutorId != null ? post.tutor.id.eq(tutorId) : null,
                        post.track.id.eq(trackId)
                )
                .orderBy(post.uploadedAt.desc())
                .fetch();
    }

    @Override
    public List<Post> findPostListInBackOfficePostTypeNotNullByPM(PostTypeEnum postType, LocalDate startDate,
                                                                  LocalDate endDate, Integer searchPeriod, Boolean isOpened, TrackNameEnum trackName, Long tutorId) {

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
                        tutorId != null ? post.tutor.id.eq(tutorId) : null,
                        post.track.trackName.eq(trackName)
                )
                .orderBy(post.uploadedAt.desc())
                .fetch();
    }

    @Override
    public List<Post> findPostListInBackOfficePostTypeNotNullApm(PostTypeEnum postType, LocalDate startDate,
                                                                 LocalDate endDate, Long trackId, Boolean isOpened, Long tutorId) {

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
                        isOpened != null ? post.isOpened.eq(isOpened) : null,
                        tutorId != null ? post.tutor.id.eq(tutorId) : null,
                        post.track.id.eq(trackId)
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

    @Override
    public List<Post> findPostListBySearchTypeContentAndKeywordAndTrack(String keyword, Long trackId) {
        QPost post = QPost.post;
        QPostTag postTag = QPostTag.postTag;
        QTag tag = QTag.tag;
        QTutor tutor = QTutor.tutor;

        return queryFactory.select(post)
                .from(post)
                .leftJoin(post.postTagList, postTag).fetchJoin()
                .leftJoin(postTag.tag, tag).fetchJoin()
                .leftJoin(post.tutor, tutor).fetchJoin()
                .where(
                        post.track.id.eq(trackId),
                        post.content.contains(keyword)
                )
                .fetch();
    }

    @Override
    public List<Post> findPostListBySearchTypeTitleAndKeywordAndTrack(String keyword, Long trackId) {
        QPost post = QPost.post;
        QPostTag postTag = QPostTag.postTag;
        QTag tag = QTag.tag;
        QTutor tutor = QTutor.tutor;

        return queryFactory.select(post)
                .from(post)
                .leftJoin(post.postTagList, postTag).fetchJoin()
                .leftJoin(postTag.tag, tag).fetchJoin()
                .leftJoin(post.tutor, tutor).fetchJoin()
                .where(
                        post.track.id.eq(trackId),
                        post.title.contains(keyword)
                )
                .fetch();
    }

    @Override
    public List<Post> findPostListBySearchTypeTutorAndKeywordAndTrack(String keyword, Long trackId) {
        QPost post = QPost.post;
        QPostTag postTag = QPostTag.postTag;
        QTag tag = QTag.tag;
        QTutor tutor = QTutor.tutor;

        return queryFactory.select(post)
                .from(post)
                .leftJoin(post.postTagList, postTag).fetchJoin()
                .leftJoin(postTag.tag, tag).fetchJoin()
                .leftJoin(post.tutor, tutor).fetchJoin()
                .where(
                        post.track.id.eq(trackId),
                        post.tutor.tutorName.eq(keyword)
                )
                .fetch();
    }

    @Override
    public List<Post> findPostListBySearchTypeTagAndKeywordAndTrack(String keyword, Long trackId) {
        QPost post = QPost.post;
        QPostTag postTag = QPostTag.postTag;
        QTag tag = QTag.tag;
        QTutor tutor = QTutor.tutor;

        return queryFactory.selectDistinct(post)
                .from(post)
                .leftJoin(post.postTagList, postTag).fetchJoin()
                .leftJoin(postTag.tag, tag).fetchJoin()
                .leftJoin(post.tutor, tutor).fetchJoin()
                .where(
                        post.id.in(
                                queryFactory.select(post.id)
                                        .from(post)
                                        .join(post.postTagList, postTag)
                                        .where(postTag.tag.tagName.contains(keyword))
                        ),
                        post.track.id.eq(trackId)
                )
                .fetch();
    }

    @Override
    public List<Post> findPostListBySearchTypeContentAndKeywordAndTrackAndPostType(PostTypeEnum postType, String keyword, Long trackId) {
        QPost post = QPost.post;
        QPostTag postTag = QPostTag.postTag;
        QTag tag = QTag.tag;
        QTutor tutor = QTutor.tutor;

        return queryFactory.selectDistinct(post)
                .from(post)
                .leftJoin(post.postTagList, postTag).fetchJoin()
                .leftJoin(postTag.tag, tag).fetchJoin()
                .leftJoin(post.tutor, tutor).fetchJoin()
                .where(
                        post.track.id.eq(trackId),
                        post.content.contains(keyword),
                        post.postType.eq(postType)
                )
                .fetch();
    }

    @Override
    public List<Post> findPostListBySearchTypeTitleAndKeywordAndTrackAndPostType(PostTypeEnum postType, String keyword, Long trackId) {
        QPost post = QPost.post;
        QPostTag postTag = QPostTag.postTag;
        QTag tag = QTag.tag;
        QTutor tutor = QTutor.tutor;

        return queryFactory.selectDistinct(post)
                .from(post)
                .leftJoin(post.postTagList, postTag).fetchJoin()
                .leftJoin(postTag.tag, tag).fetchJoin()
                .leftJoin(post.tutor, tutor).fetchJoin()
                .where(
                        post.track.id.eq(trackId),
                        post.title.contains(keyword),
                        post.postType.eq(postType)
                )
                .fetch();
    }

    @Override
    public List<Post> findPostListBySearchTypeTagAndKeywordAndTrackAndPostType(PostTypeEnum postType, String keyword, Long trackId) {
        QPost post = QPost.post;
        QPostTag postTag = QPostTag.postTag;
        QTag tag = QTag.tag;
        QTutor tutor = QTutor.tutor;

        return queryFactory.selectDistinct(post)
                .from(post)
                .leftJoin(post.postTagList, postTag).fetchJoin()
                .leftJoin(postTag.tag, tag).fetchJoin()
                .leftJoin(post.tutor, tutor).fetchJoin()
                .where(
                        post.id.in(
                                queryFactory.select(post.id)
                                        .from(post)
                                        .join(post.postTagList, postTag)
                                        .where(postTag.tag.tagName.contains(keyword))
                        ),
                        post.postType.eq(postType),
                        post.track.id.eq(trackId)
                )
                .fetch();
    }

    @Override
    public List<Post> findPostListBySearchTypeTutorAndKeywordAndTrackAndPostType(PostTypeEnum postType, String keyword, Long trackId) {
        QPost post = QPost.post;
        QPostTag postTag = QPostTag.postTag;
        QTag tag = QTag.tag;
        QTutor tutor = QTutor.tutor;

        return queryFactory.select(post)
                .from(post)
                .leftJoin(post.postTagList, postTag).fetchJoin()
                .leftJoin(postTag.tag, tag).fetchJoin()
                .leftJoin(post.tutor, tutor).fetchJoin()
                .where(
                        post.track.id.eq(trackId),
                        post.tutor.tutorName.eq(keyword),
                        post.postType.eq(postType)
                )
                .fetch();
    }
}
