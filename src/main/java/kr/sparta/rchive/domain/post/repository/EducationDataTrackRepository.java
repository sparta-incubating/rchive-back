package kr.sparta.rchive.domain.post.repository;

import java.util.List;
import kr.sparta.rchive.domain.post.entity.EducationDataTrack;
import kr.sparta.rchive.domain.post.entity.EducationDataTrackId;
import kr.sparta.rchive.domain.user.enums.TrackEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EducationDataTrackRepository extends JpaRepository<EducationDataTrack, EducationDataTrackId> {
    EducationDataTrack findByEducationData_Id(Long educationDataId);
}
