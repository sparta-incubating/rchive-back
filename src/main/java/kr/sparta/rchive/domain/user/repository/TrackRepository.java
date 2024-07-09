package kr.sparta.rchive.domain.user.repository;

import java.util.List;
import java.util.Optional;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, Long> {

    List<Track> findAllByTrackName(TrackNameEnum trackName);

    Optional<Track> findTrackByTrackNameAndPeriod(TrackNameEnum trackName, Integer period);
}
