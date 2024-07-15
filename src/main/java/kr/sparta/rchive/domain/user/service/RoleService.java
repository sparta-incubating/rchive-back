package kr.sparta.rchive.domain.user.service;

import java.util.ArrayList;
import java.util.List;
import kr.sparta.rchive.domain.user.dto.request.RoleRequestListReq;
import kr.sparta.rchive.domain.user.dto.request.RoleRequestReq;
import kr.sparta.rchive.domain.user.entity.Role;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.AuthEnum;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.domain.user.enums.TrackRoleEnum;
import kr.sparta.rchive.domain.user.exception.RoleCustomException;
import kr.sparta.rchive.domain.user.exception.RoleExceptionCode;
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
    public void requestRole(User user, Track track, TrackRoleEnum trackRole) {
        Role role = Role.builder()
                .user(user)
                .track(track)
                .trackRole(trackRole)
                .auth(AuthEnum.WAIT)
                .build();

        roleRepository.save(role);
    }

    public Role getRoleByManager(User user){
        List<Role> roleList = findAllByUserIdApprove(user.getId());
        Role role = null;
        for(Role r : roleList){
            if (r.getTrackRole() == TrackRoleEnum.PM){
                role = r;
                break;
            }else if(r.getTrackRole() == TrackRoleEnum.APM){
                role = r;
                break;
            }
        }

        if(role == null){
            throw new RoleCustomException(RoleExceptionCode.BAD_REQUEST_NO_ROLE);
        }

        return role;
    }

    public void approveRoleRequest(List<RoleRequestListReq> reqList){
        List<Role> roleList = findRoleListByEmailAndTrackNameAndPeriodAndTrackRole(reqList);
        for(Role role: roleList){
            role.approveAuth();
        }
        roleRepository.saveAll(roleList);
    }

    public AuthEnum getResultRoleFirstLogin(User user) {

        Role role = roleRepository.findFirstByUserIdOrderByCreatedAtAsc(user.getId()).orElseThrow(
                () -> new RoleCustomException(RoleExceptionCode.NOT_FOUND_ROLE_REQUEST));
        return role.getAuth();
    }

    public boolean getRequestRoleFirstLogin(User user) {
        return roleRepository.existsRoleByUserId(user.getId());
    }

    // 트랙 ID와 유저의 ID로 권한을 찾아오는 로직
    public TrackRoleEnum findTrackRoleByTrackIdAndUserId(Long userTrackId, Long userId) {
        Role userRole = findRoleByUserIdAndTrackId(userId, userTrackId);

        return userRole.getTrackRole();
    }

    public Role findRoleByUserIdAndTrackId(Long userId, Long trackId) {
        return roleRepository.findByUserIdAndTrackId(userId, trackId).orElseThrow(
                () -> new RoleCustomException(RoleExceptionCode.NOT_FOUND_ROLE_REQUEST)
        );
    }

    public List<Role> findAllByUserIdApprove(Long userId) {
        return roleRepository.findAllByUserIdAndAuth(userId, AuthEnum.APPROVE);
    }

    public List<Role> findRoleListByEmailAndTrackNameAndPeriodAndTrackRole(List<RoleRequestListReq> reqList) {
        List<Role> roleList = new ArrayList<>();
        for(RoleRequestListReq req : reqList){
            roleList.add(findRoleByEmailAndTrackNameAndPeriodAndTrackRole(req.email(), req.trackName(), req.period(), req.trackRole()));
        }
        return roleList;
    }

    public Role findRoleByEmailAndTrackNameAndPeriodAndTrackRole(String email, TrackNameEnum trackName, int period, TrackRoleEnum trackRole) {
        return roleRepository.findByEmailAndTrackNameAndPeriodAndTrackRole(email, trackName, period, trackRole).orElseThrow(
                () -> new RoleCustomException(RoleExceptionCode.BAD_REQUEST_NO_ROLE_REQUEST_LIST)
        );
    }
}
