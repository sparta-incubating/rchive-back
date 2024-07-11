package kr.sparta.rchive.domain.user.repository;

import java.util.Optional;
import kr.sparta.rchive.domain.user.entity.Role;
import kr.sparta.rchive.domain.user.entity.RoleId;
import kr.sparta.rchive.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, RoleId> {

    boolean existsRoleByUser(User user);

    Role findByTrackIdAndUserId(Long trackId, Long userId);
}
