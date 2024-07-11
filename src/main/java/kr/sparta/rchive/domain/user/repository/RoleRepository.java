package kr.sparta.rchive.domain.user.repository;

import java.util.List;
import java.util.Optional;
import kr.sparta.rchive.domain.user.entity.Role;
import kr.sparta.rchive.domain.user.entity.RoleId;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.AuthEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, RoleId> {

    Optional<Role> findFirstByUserIdOrderByCreatedAtAsc(Long userId);

    boolean existsRoleByUserId(Long userId);

    Role findByTrackIdAndUserId(Long trackId, Long userId);

    List<Role> findAllByUserIdAndAuth(Long userId, AuthEnum auth);
}
