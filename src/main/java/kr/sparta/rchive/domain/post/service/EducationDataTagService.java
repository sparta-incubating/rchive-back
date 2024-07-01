package kr.sparta.rchive.domain.post.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.sparta.rchive.domain.post.entity.EducationDataTag;
import kr.sparta.rchive.domain.post.repository.EducationDataTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EducationDataTagService {

    private final EducationDataTagRepository educationDataTagRepository;

    // 교육자료태그 테이블에서 태그 ID를 이용하여 태그가 붙어있는 교육자료들을 List로 가져오는 로직
    public List<Long> findEducationDataIdByTagIdAndIsDeletedFalse(Long tagId) {
        return educationDataTagRepository.findEducationDataTagListByTagIdAndIsDeletedFalse(tagId)
                .stream()
                .map(educationDataTag -> educationDataTag.getEducationData().getId())
                .toList();
    }

    // 교육자료에 붙어있는 모든 태그들을 가져오는 로직
    public List<EducationDataTag> findEducationDataTagByEducationDataIdList(List<Long> educationDataIdList) {
        return educationDataTagRepository.findByEducationDataIdIn(educationDataIdList);
    }

    public Map<Long, List<Long>> findEducationDataTagListByTagId(List<Long> educationDataIdList) {
        List<EducationDataTag> educationDataTagList = findEducationDataTagByEducationDataIdList(educationDataIdList);

        // 교육자료 ID를 Key로 태그 List를 Value로 갖는 Map 형식으로 변환
        return educationDataTagList.stream()
                .collect(Collectors.groupingBy(
                        educationDataTag -> educationDataTag.getEducationData().getId(),
                        Collectors.mapping(educationDataTag -> educationDataTag.getTag().getId(), Collectors.toList())
                ));
    }
}
