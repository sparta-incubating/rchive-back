package kr.sparta.rchive.domain.post.service;

import java.util.List;
import java.util.stream.Collectors;
import kr.sparta.rchive.domain.post.entity.EducationData;
import kr.sparta.rchive.domain.post.exception.PostCustomExeption;
import kr.sparta.rchive.domain.post.exception.PostExceptionCode;
import kr.sparta.rchive.domain.post.repository.EducationDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EducationDataService {

    private final EducationDataRepository educationDataRepository;
    
    // 교육자료 테이블에서 ID를 이용하여 검색하는 로직
    public EducationData findEducationDataById(Long educationDataId) {
        return educationDataRepository.findById(educationDataId).orElseThrow(
                () -> new PostCustomExeption(PostExceptionCode.NOT_FOUND_POST_NOT_EXIST)
        );
    }
}
