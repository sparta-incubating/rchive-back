package kr.sparta.rchive.domain.post.repository;


import java.util.List;
import kr.sparta.rchive.domain.post.entity.EducationDataTag;
import kr.sparta.rchive.domain.post.entity.EducationDataTagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EducationDataTagRepository extends JpaRepository<EducationDataTag, EducationDataTagId> {

    @Query("select edt.educationData.id from EducationDataTag edt join fetch Tag t on edt.tag.Id = t.Id "
            + "join fetch EducationData e on edt.educationData.id = e.id "
            + "where t.Id = :tagId and e.isDeleted = false")
    List<EducationDataTag> findEducationDataTagListByTagId(Long tagId);

    @Query("select edt from EducationDataTag edt where edt.educationData.id in :educationDataList")
    List<EducationDataTag> findByEducationDataIdIn(List<Long> educationDataList);
}
