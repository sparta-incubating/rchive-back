package kr.sparta.rchive.domain.user.repository;

import java.util.List;
import java.util.Optional;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TrackRepository extends JpaRepository<Track, Long> {

    @Query("select t from Track t "
            + "where t.trackName = :trackName "
            + "and t.period != 0 "
            + "order by t.period desc")
    List<Track> findAllByTrackName(TrackNameEnum trackName);

    Optional<Track> findTrackByTrackNameAndPeriod(TrackNameEnum trackName, Integer period);
}
