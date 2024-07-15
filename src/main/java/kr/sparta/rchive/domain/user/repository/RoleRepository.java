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

}
