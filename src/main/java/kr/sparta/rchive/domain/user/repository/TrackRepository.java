package kr.sparta.rchive.domain.user.repository;

import kr.sparta.rchive.domain.user.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, Long> {

}
