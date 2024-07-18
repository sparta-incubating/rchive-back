package kr.sparta.rchive.domain.core.service;

import java.util.List;
import kr.sparta.rchive.domain.user.dto.request.RoleRequestListReq;
import kr.sparta.rchive.domain.user.dto.request.RoleRequestReq;
import kr.sparta.rchive.domain.user.dto.response.RoleGetLastSelectRoleRes;
import kr.sparta.rchive.domain.user.dto.response.RoleGetTrackRoleRequestCountRes;
import kr.sparta.rchive.domain.user.dto.response.UserRes;
import kr.sparta.rchive.domain.user.entity.Role;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.AuthEnum;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.domain.user.enums.TrackRoleEnum;
import kr.sparta.rchive.domain.user.exception.RoleCustomException;
import kr.sparta.rchive.domain.user.exception.RoleExceptionCode;
import kr.sparta.rchive.domain.user.exception.UserCustomException;
import kr.sparta.rchive.domain.user.service.RoleService;
import kr.sparta.rchive.domain.user.service.TrackService;
import kr.sparta.rchive.domain.user.service.UserService;
import kr.sparta.rchive.global.execption.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserTrackRoleCoreService {
    private final UserService userService;
    private final RoleService roleService;
    private final TrackService trackService;

    public void requestRole(User user, RoleRequestReq req){
        Track track = trackService.findTrackByTrackNameAndPeriod(req.trackName(),req.period());
        roleService.requestRole(user, track, req.trackRole());
    }

    public RoleGetLastSelectRoleRes getLastSelectRoleBackoffice(User user){

        Role role = roleService.getRoleByManager(user);
        Track track = role.getTrack();

        return RoleGetLastSelectRoleRes.builder()
                .trackRole(role.getTrackRole())
                .trackName(track.getTrackName())
                .period(track.getPeriod())
                .build();
    }

    public UserRes getProfile(User user) {

        Role role = roleService.getRoleByManager(user);
        Track track = role.getTrack();

        return UserRes.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .birth(user.getBirth())
                .phone(user.getPhone())
                .trackRole(role.getTrackRole())
                .trackName(track.getTrackName())
                .period(track.getPeriod())
                .build();
    }

    @Transactional
    public void userTrackRoleApprove(User manager, List<RoleRequestListReq> reqList) {
        Track managaerTrack = roleService.getRoleByManager(manager).getTrack();
        managerTrackRoleInvalid(managaerTrack, reqList);

        roleService.approveRoleRequest(reqList);
        roleService.deleteRoleListByAuthNotApprove(reqList);
    }

    @Transactional
    public void userTrackRoleReject(User manager, List<RoleRequestListReq> reqList) {
        Track managaerTrack = roleService.getRoleByManager(manager).getTrack();
        managerTrackRoleInvalid(managaerTrack, reqList);

        roleService.rejectRoleRequest(reqList);
    }

    public void managerTrackRoleInvalid(Track managaerTrack, List<RoleRequestListReq> reqList) {
        /* PM */
        if(managaerTrack.getPeriod()==0){
            for(RoleRequestListReq req: reqList){
                if(req.trackName() != managaerTrack.getTrackName()){
                    throw new RoleCustomException(RoleExceptionCode.FORBIDDEN_TRACK_NOT_ACCESS);
                }
            }
        }
        /* APM */
        else{
            for(RoleRequestListReq req: reqList){
                if(req.trackName() != managaerTrack.getTrackName() || req.period() != managaerTrack.getPeriod()){
                    throw new RoleCustomException(RoleExceptionCode.FORBIDDEN_TRACK_NOT_ACCESS);
                }
            }
        }
    }

    public RoleGetTrackRoleRequestCountRes getTrackRoleRequestCount(User user, TrackNameEnum trackName, Integer period) {

        Role role = roleService.getRoleByManager(user);

        if(role.getTrackRole() == TrackRoleEnum.PM){

            roleService.existByUserAndTrackNameByPmThrowsException(user.getId(), trackName);

            return RoleGetTrackRoleRequestCountRes.builder()
                    .statusAll(roleService.countByTrackNameAndAuthNotRejectByPm(trackName))
                    .statusWait(roleService.countByTrackNameAndAuthByPm(trackName, AuthEnum.WAIT))
                    .statusApprove(roleService.countByTrackNameAndAuthByPm(trackName, AuthEnum.APPROVE))
                    .build();
        }

        if(period==null){
            throw new RoleCustomException(RoleExceptionCode.BAD_REQUEST_NO_PARAMETER_PERIOD);
        }

        roleService.existByUserAndTrackNameAndPeriodByApmThrowsException(user.getId(), trackName, period);

        return RoleGetTrackRoleRequestCountRes.builder()
                .statusAll(roleService.countByTrackNameAndPeriodAndAuthNotRejectByApm(trackName, period))
                .statusWait(roleService.countByTrackNameAndPeriodAndAuthByApm(trackName, period, AuthEnum.WAIT))
                .statusApprove(roleService.countByTrackNameAndPeriodAndAuthByApm(trackName, period, AuthEnum.APPROVE))
                .build();

    }

}
