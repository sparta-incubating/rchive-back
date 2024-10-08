package kr.sparta.rchive.domain.user.service;

import java.util.ArrayList;
import java.util.List;
import kr.sparta.rchive.domain.user.dto.request.RoleRequestListReq;
import kr.sparta.rchive.domain.user.entity.Role;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.AuthEnum;
import kr.sparta.rchive.domain.user.enums.OrderRoleListEnum;
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

    public Role getRoleByManager(User user) {
        List<Role> roleList = findRoleListByUserIdAuthApprove(user.getId());
        Role role = null;
        for (Role r : roleList) {
            if (r.getTrackRole() == TrackRoleEnum.PM) {
                role = r;
                break;
            } else if (r.getTrackRole() == TrackRoleEnum.APM) {
                role = r;
                break;
            }
        }

        if (role == null) {
            throw new RoleCustomException(RoleExceptionCode.BAD_REQUEST_NO_ROLE);
        }

        return role;
    }

    public void approveRoleRequest(List<RoleRequestListReq> reqList) {
        List<Role> roleList = findRoleListByEmailAndTrackNameAndPeriodAndTrackRole(reqList);
        for (Role role : roleList) {
            role.approveAuth();
        }
        roleRepository.saveAll(roleList);
    }

    public void rejectRoleRequest(List<RoleRequestListReq> reqList) {
        List<Role> roleList = findRoleListByEmailAndTrackNameAndPeriodAndTrackRole(reqList);
        for (Role role : roleList) {
            role.rejectAuth();
        }
        roleRepository.saveAll(roleList);
    }

    public void deleteRoleListByAuthReject(List<RoleRequestListReq> reqList) {
        List<Role> roleList = findRoleListByEmailAndAuthReject(reqList);
        roleRepository.deleteAll(roleList);
    }

    public void deleteApmRoleListExceptLastApprove(List<RoleRequestListReq> reqList) {
        List<String> apmEmailList = new ArrayList<>();
        for (RoleRequestListReq req : reqList) {
            if (req.trackRole() == TrackRoleEnum.APM) {
                if (!apmEmailList.contains(req.email())) {
                    apmEmailList.add(req.email());
                }
            }
        }

        List<Role> roleList = findRoleListByEmailAndTrackRoleExceptLastApprove(apmEmailList);
        roleRepository.deleteAll(roleList);
    }

    public AuthEnum getResultRoleFirstLogin(User user) {

        Role role = roleRepository.findFirstByUserIdOrderByCreatedAtDesc(user.getId()).orElseThrow(
                () -> new RoleCustomException(RoleExceptionCode.NOT_FOUND_ROLE));
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
                () -> new RoleCustomException(RoleExceptionCode.NOT_FOUND_ROLE)
        );
    }

    public List<Role> findRoleListByUserIdAuthApprove(Long userId) {
        return roleRepository.findAllByUserIdAndAuth(userId, AuthEnum.APPROVE);
    }

    public List<Role> findRoleListByEmailAndTrackNameAndPeriodAndTrackRole(
            List<RoleRequestListReq> reqList) {
        List<Role> roleList = new ArrayList<>();
        for (RoleRequestListReq req : reqList) {
            roleList.add(
                    findRoleByEmailAndTrackNameAndPeriodAndTrackRole(req.email(), req.trackName(),
                            req.period(), req.trackRole()));
        }
        return roleList;
    }

    public Role findRoleByEmailAndTrackNameAndPeriodAndTrackRole(String email,
            TrackNameEnum trackName, int period, TrackRoleEnum trackRole) {
        return roleRepository.findByEmailAndTrackNameAndPeriodAndTrackRole(email, trackName, period,
                trackRole).orElseThrow(
                () -> new RoleCustomException(RoleExceptionCode.BAD_REQUEST_NO_ROLE_REQUEST_LIST)
        );
    }

    public List<Role> findRoleListByEmailAndAuthReject(List<RoleRequestListReq> reqList) {
        List<Role> roleList = new ArrayList<>();
        for (RoleRequestListReq req : reqList) {
            List<Role> rejectList = roleRepository.findAllByEmailAndAuth(req.email(),
                    AuthEnum.REJECT);
            roleList.addAll(rejectList);
        }
        return roleList;
    }

    public List<Role> findRoleListByEmailAndTrackRoleExceptLastApprove(List<String> emailList) {
        List<Role> roleList = new ArrayList<>();
        for (String email : emailList) {
            List<Role> apmList = roleRepository.findRoleListByEmailAndTrackRoleAndApprove(
                    email, TrackRoleEnum.APM);
            if (!apmList.isEmpty()) {
                apmList.remove(apmList.size() - 1);
            }
            roleList.addAll(apmList);
        }
        return roleList;
    }

    public int countByRoleRequestByPm(
            TrackNameEnum trackName, Integer searchPeriod, AuthEnum auth) {
        return roleRepository.countByRoleRequestByPm(trackName, searchPeriod, auth);
    }

    public int countByRoleRequestByApm(
            TrackNameEnum trackName, int period, AuthEnum auth) {
        return roleRepository.countByRoleRequestByApm(trackName, period, auth);
    }

    public void existByUserAndTrackByPmThrowException(Long userId, TrackNameEnum trackName) {
        if (!roleRepository.existsByUserIdAndTracNameAndAuthApproveByPm(userId, trackName)) {
            throw new RoleCustomException(RoleExceptionCode.FORBIDDEN_ROLE);
        }
    }

    public boolean existByUserAndTrackByPm(Long userId, TrackNameEnum trackName) {
        return roleRepository.existsByUserIdAndTracNameAndAuthApproveByPm(userId, trackName);
    }

    public void existByUserAndTrackByApmThrowException(Long userId, Long trackId) {
        if (!roleRepository.existsByUserIdAndTracIdAndAuthApproveByApm(userId, trackId)) {
            throw new RoleCustomException(RoleExceptionCode.FORBIDDEN_ROLE);
        }
    }

    public List<Role> findRoleListInBackOfficeAuthNoReject(
            Track managerTrack, Integer searchPeriod, String keyword, TrackRoleEnum trackRole,
            OrderRoleListEnum sort) {
        if (managerTrack.getPeriod() == 0) {
            return roleRepository.findRoleListInBackOfficeAuthNoRejectByPm(
                    managerTrack.getTrackName(), searchPeriod, keyword, trackRole, sort);
        }
        return roleRepository.findRoleListInBackOfficeAuthNoRejectByApm(
                managerTrack.getTrackName(), managerTrack.getPeriod(), keyword, trackRole, sort);
    }

    public List<Role> findRoleListInBackOffice(
            Track managerTrack, Integer searchPeriod, AuthEnum auth, String keyword,
            TrackRoleEnum trackRole, OrderRoleListEnum sort) {
        if (managerTrack.getPeriod() == 0) {
            return roleRepository.findRoleListInBackOfficeByPm(
                    managerTrack.getTrackName(), searchPeriod, auth, keyword, trackRole, sort);
        }
        return roleRepository.findRoleListInBackOfficeByApm(
                managerTrack.getTrackName(), managerTrack.getPeriod(), auth, keyword, trackRole,
                sort);
    }


    public boolean checkIsPm(Long userId, TrackNameEnum trackName) {
        return roleRepository.existsByUserIdAndTracNameAndAuthApproveByPm(userId, trackName);
    }
}
