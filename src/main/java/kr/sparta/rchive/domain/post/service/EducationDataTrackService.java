package kr.sparta.rchive.domain.post.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kr.sparta.rchive.domain.post.entity.EducationDataTrack;
import kr.sparta.rchive.domain.post.repository.EducationDataTrackRepository;
import kr.sparta.rchive.domain.user.enums.TrackEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EducationDataTrackService {

    private final EducationDataTrackRepository educationDataTrackRepository;

    // 교육자료 List를 트랙 ID로 조회하여 필요한 데이터만 남기는 로직
    public List<Long> filterEducationDataIdListByTrackId(List<Long> educationDataIdList, Long userTrackId) {
        return educationDataIdList.stream().filter(
                educationData -> {
                    EducationDataTrack edt = findByEducationDataId(educationData);
                    return Objects.equals(edt.getTrack().getId(), userTrackId);
                }
        ).collect(Collectors.toList());
    }

    // 교육자료 List를 트랙 이름으로 조회하여 필요한 데이터만 남기는 로직
    public List<Long> filterEducationDataIdListByTrackName(List<Long> educationDataIdList, TrackEnum trackName) {
        return educationDataIdList.stream().filter(
                educationData -> {
                    EducationDataTrack edt = findByEducationDataId(educationData);
                    return edt.getTrack().getTrack().equals(trackName);
                }
        ).collect(Collectors.toList());
    }

    // 교육자료트랙 테이블에서 교육자료 ID를 이용하여 교육자료트랙 데이터를 검색해오는 로직
    public EducationDataTrack findByEducationDataId(Long educationDataId) {
        return educationDataTrackRepository.findByEducationDataId(educationDataId);
    }
}