package kr.sparta.rchive.domain.core.service;

import java.util.List;
import kr.sparta.rchive.domain.user.dto.request.RoleRequestListReq;
import kr.sparta.rchive.domain.user.dto.request.RoleRequestReq;
import kr.sparta.rchive.domain.user.dto.response.RoleGetLastSelectRoleRes;
import kr.sparta.rchive.domain.user.dto.response.UserRes;
import kr.sparta.rchive.domain.user.entity.Role;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.entity.User;
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

        // TODO: 전에 Reject 한 내역 강한 삭제

        roleService.approveRoleRequest(reqList);
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


}
