package kr.sparta.rchive.domain.post.repository;

import java.util.List;
import kr.sparta.rchive.domain.post.entity.Tutor;

public interface TutorRepositoryCustom {

    List<Tutor> findTutorList(String tutorName, Long trackId);
}
