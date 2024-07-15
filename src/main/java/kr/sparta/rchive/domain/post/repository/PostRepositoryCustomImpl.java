package kr.sparta.rchive.domain.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.sparta.rchive.domain.post.entity.QPost;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Long> findPostIdListByOption(PostTypeEnum postType, LocalDate uploadedAt, Integer period, String tutor, Boolean isOpened) {
        QPost post = QPost.post;

        return queryFactory.select(post.id)
                .from(post)
                .where(
                        postType != null ? post.postType.eq(postType) : null,
                        uploadedAt != null ? post.uploadedAt.eq(uploadedAt) : null,
                        period != null ? post.track.period.eq(period) : null,
                        tutor != null ? post.tutor.eq(tutor) : null,
                        isOpened != null ? post.isOpened.eq(isOpened) : null
                )
                .fetch();
    }

    @Override
    public List<Long> findPostIdListInBackOfficeByPM(PostTypeEnum postType, LocalDate uploadedAt,
                                                     String tutor, Boolean isOpened) {
        QPost post = QPost.post;

        return queryFactory.select(post.id)
                .from(post)
                .where(
                        postType != null ? post.postType.eq(postType) : null,
                        uploadedAt != null ? post.uploadedAt.eq(uploadedAt) : null,
                        tutor != null ? post.tutor.eq(tutor) : null,
                        isOpened != null ? post.isOpened.eq(isOpened) : null
                ).fetch();
    }
}
