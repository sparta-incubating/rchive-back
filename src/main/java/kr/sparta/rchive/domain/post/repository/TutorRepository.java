package kr.sparta.rchive.domain.post.repository;

import kr.sparta.rchive.domain.post.entity.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TutorRepository extends JpaRepository<Tutor, Long> {

    @Query("select t from Tutor t where t.tutorName like %:tutorName% " +
            "and t.track.id = :trackId")
    List<Tutor> findTutorListByTutorNameAndTrackId(String tutorName, Long trackId);
}
