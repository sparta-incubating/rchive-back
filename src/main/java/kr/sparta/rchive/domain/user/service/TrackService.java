package kr.sparta.rchive.domain.user.service;

import kr.sparta.rchive.domain.user.entity.Track;
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
    public boolean checkPermission(Long userTrackId) {
        Track findTrack = findTrackById(userTrackId);
        return findTrack.getIsPermission();
    }

    // 트랙 ID로 트랙을 검색해오는 로직
    public Track findTrackById(Long userTrackId) {
        return trackRepository.findById(userTrackId).orElseThrow(
                () -> new IllegalArgumentException() // 추후 커스텀 에러로 변경할 예정
        );
    }
}
