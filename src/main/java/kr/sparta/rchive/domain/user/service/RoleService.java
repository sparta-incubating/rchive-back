package kr.sparta.rchive.domain.user.service;

import kr.sparta.rchive.domain.user.dto.request.RoleRequestReq;
import kr.sparta.rchive.domain.user.entity.Role;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.AuthEnum;
import kr.sparta.rchive.domain.user.enums.TrackRoleEnum;
import kr.sparta.rchive.domain.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class RoleService {

    private final RoleRepository roleRepository;

    @Transactional
    public void requestRole(User user, Track track, TrackRoleEnum trackRole){
        Role role = Role.builder()
                .user(user)
                .track(track)
                .trackRole(trackRole)
                .auth(AuthEnum.WAIT)
                .build();

        roleRepository.save(role);
    }

    public boolean getRequestRole(User user){
        return roleRepository.existsRoleByUserId(user.getId());
    }

    // 트랙 ID와 유저의 ID로 권한을 찾아오는 로직
    public TrackRoleEnum findTrackRoleByTrackIdAndUserId(Long userTrackId, Long userId) {
        Role userRole = roleRepository.findByTrackIdAndUserId(userTrackId, userId);

        return userRole.getTrackRole();
    }
}
