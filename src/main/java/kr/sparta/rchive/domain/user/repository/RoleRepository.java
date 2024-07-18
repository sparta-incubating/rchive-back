package kr.sparta.rchive.domain.user.repository;

import kr.sparta.rchive.domain.user.entity.Role;
import kr.sparta.rchive.domain.user.entity.RoleId;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.AuthEnum;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.domain.user.enums.TrackRoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, RoleId> {
    Optional<Role> findByUserIdAndTrackId(Long trackId, Long userId);

    Optional<Role> findFirstByUserIdOrderByCreatedAtAsc(Long userId);

    boolean existsRoleByUserId(Long userId);

    List<Role> findAllByUserIdAndAuth(Long userId, AuthEnum auth);

    @Query("select r from Role r "
            + "where r.user.email = :email "
            + "and r.track.trackName = :trackName "
            + "and r.track.period = :period "
            + "and r.trackRole = :trackRole")
    Optional<Role> findByEmailAndTrackNameAndPeriodAndTrackRole(String email, TrackNameEnum trackName, int period, TrackRoleEnum trackRole);

    @Query("select r from Role r "
            + "where r.user.email = :email "
            + "and r.auth = :auth")
    List<Role> findAllByEmailAndAuth(String email, AuthEnum auth);

    /* PM */
    @Query("select count(r) from Role r "
            + "where r.track.trackName = :trackName "
            + "and r.track.period != 0 "
            + "and r.auth = :auth")
    int countAllByTrackNameAndAuthByPm(TrackNameEnum trackName, AuthEnum auth);

    /* PM */
    @Query("select count(r) from Role r "
            + "where r.track.trackName = :trackName "
            + "and r.track.period != 0 "
            + "and r.auth != 'REJECT'")
    int countAllByTrackNameAndAuthNotRejectByPm(TrackNameEnum trackName);

    /* APM */
    @Query("select count(r) from Role r "
            + "where r.track.trackName = :trackName "
            + "and r.track.period = :period "
            + "and r.auth = :auth")
    int countAllByTrackNameAndPeriodAndAuthByApm(TrackNameEnum trackName, int period, AuthEnum auth);

    /* APM */
    @Query("select count(r) from Role r "
            + "where r.track.trackName = :trackName "
            + "and r.track.period = :period "
            + "and r.auth != 'REJECT'")
    int countAllByTrackNameAndPeriodAndAuthNotRejectByApm(TrackNameEnum trackName, int period);

    /* PM */
    @Query("select count(r) > 0 from Role r "
            + "where r.track.trackName = :trackName "
            + "and r.user.id = :userId "
            + "and r.auth = 'APPROVE'")
    boolean existsByUserIdAndTrackNameAndAuthApproveByPm(Long userId, TrackNameEnum trackName);

    /* APM */
    @Query("select count(r) > 0 from Role r "
            + "where r.track.trackName = :trackName "
            + "and r.track.period = :period "
            + "and r.user.id = :userId "
            + "and r.auth = 'APPROVE'")
    boolean existsByUserIdAndTrackNameAndPeriodAndAuthApproveByApm(Long userId, TrackNameEnum trackName, int period);

}
