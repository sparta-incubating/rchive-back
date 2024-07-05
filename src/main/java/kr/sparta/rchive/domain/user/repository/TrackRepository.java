package kr.sparta.rchive.domain.user.repository;

import java.util.Optional;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.enums.TrackEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, Long> {
    Optional<Track> findTrackByTrackNameAndPeriod(TrackEnum trackName, Integer period);
}
