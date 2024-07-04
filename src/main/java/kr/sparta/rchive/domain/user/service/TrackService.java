package kr.sparta.rchive.domain.user.service;

import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.enums.TrackEnum;
import kr.sparta.rchive.domain.user.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrackService {

    private final TrackRepository trackRepository;

    // 트랙 안의 열람권한을 확인하는 로직
    public boolean checkPermission(Long trackId) {
        Track findTrack = findTrackById(trackId);
        return findTrack.getIsPermission();
    }

    // 트랙 ID로 트랙을 검색해오는 로직
    public Track findTrackById(Long trackId) {
        return trackRepository.findById(trackId).orElseThrow(
                () -> new IllegalArgumentException() // TODO: 추후에 커스텀 에러 코드로 변경
        );
    }

    public Track findTrackByTrackNameAndPeriod(TrackEnum trackName, Integer period) {
        return trackRepository.findTrackByTrackNameAndPeriod(trackName, period);
    }
}
