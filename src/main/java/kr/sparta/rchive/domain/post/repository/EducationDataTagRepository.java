package kr.sparta.rchive.domain.post.repository;


import java.util.List;
import kr.sparta.rchive.domain.post.entity.EducationDataTag;
import kr.sparta.rchive.domain.post.entity.EducationDataTagId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EducationDataTagRepository extends JpaRepository<EducationDataTag, EducationDataTagId> {
    List<EducationDataTag> findByTag_Id(Long tagId);

    List<EducationDataTag> findByEducationData_IdIn(List<Long> educationDataList);
}
