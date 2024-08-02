package kr.sparta.rchive.domain.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import kr.sparta.rchive.domain.post.entity.QTutor;
import kr.sparta.rchive.domain.post.entity.Tutor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TutorRepositoryCustomImpl implements TutorRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Tutor> findTutorList(String tutorName, Long trackId) {
        QTutor tutor = QTutor.tutor;

        return queryFactory
            .select(tutor)
            .from(tutor)
            .where(
                tutorName != null ? tutor.tutorName.like(tutorName) : null,
                tutor.track.id.eq(trackId)
            )
            .fetch();
    }
}
