package kr.sparta.rchive.domain.user.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import kr.sparta.rchive.domain.user.dto.TrackNameInfo;
import kr.sparta.rchive.domain.user.dto.response.RoleGetTrackNameListRes;
import kr.sparta.rchive.domain.user.dto.response.RoleGetTrackPeriodListRes;
import kr.sparta.rchive.domain.user.dto.response.RoleRes;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.domain.user.exception.TrackCustomException;
import kr.sparta.rchive.domain.user.exception.TrackExceptionCode;
import kr.sparta.rchive.domain.user.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TrackService {

    private final TrackRepository trackRepository;

    public RoleGetTrackNameListRes getTrackNameList() {
        List<TrackNameInfo> trackNameList = new ArrayList<>();

        for (TrackNameEnum trackName : TrackNameEnum.values()) {
            trackNameList.add(TrackNameInfo.of(trackName));
        }

        return new RoleGetTrackNameListRes(trackNameList);
    }

    public RoleGetTrackPeriodListRes getTrackPeriodList(TrackNameEnum trackName) {
        List<Track> trackList = trackRepository.findAllByTrackName(trackName);
        List<Integer> trackPeriodList = trackList.stream()
                .map(Track::getPeriod)
                .filter(period -> period != 0)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        return new RoleGetTrackPeriodListRes(trackPeriodList);
    }


    // 트랙 안의 열람권한을 확인하는 로직
    public boolean checkPermission(Long trackId) {
        Track findTrack = findTrackById(trackId);
        return findTrack.getIsPermission();
    }

    // 트랙 ID로 트랙을 검색해오는 로직
    public Track findTrackById(Long trackId) {
        return trackRepository.findById(trackId).orElseThrow(
                () -> new TrackCustomException(TrackExceptionCode.NOT_FOUND_TRACK)
        );
    }

    public Track findTrackByTrackNameAndPeriod(TrackNameEnum trackName, Integer period) {
        return trackRepository.findTrackByTrackNameAndPeriod(trackName, period).orElseThrow(
                () -> new TrackCustomException(TrackExceptionCode.NOT_FOUND_TRACK)
        );
    }

    public Integer findPeriodByTrackId(Long trackId) {
        return findTrackById(trackId).getPeriod();
    }

    public List<Track> findTrackListByTrackName(TrackNameEnum trackName) {
        return trackRepository.findAllByTrackName(trackName);
    }

    public void trackPermissionTrue(Long trackId) {
        Track track = findTrackById(trackId);

        track.trackPermissionTrue();

        trackRepository.save(track);
    }

    public void trackRejection(Long trackId) {
        Track track = findTrackById(trackId);

        track.trackPermissionFalse();

        trackRepository.save(track);
    }
}
