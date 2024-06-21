package kr.sparta.rchive.domain.post.repository;

import kr.sparta.rchive.domain.post.entity.EducationDataTrack;
import kr.sparta.rchive.domain.post.entity.EducationDataTrackId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EducationDataTrackRepository extends JpaRepository<EducationDataTrack, EducationDataTrackId> {

}
