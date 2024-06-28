package kr.sparta.rchive.domain.user.repository;

import kr.sparta.rchive.domain.user.entity.Role;
import kr.sparta.rchive.domain.user.entity.RoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, RoleId> {
    Role findByTrack_IdAndUser_Id(Long trackId, Long userId);
}
