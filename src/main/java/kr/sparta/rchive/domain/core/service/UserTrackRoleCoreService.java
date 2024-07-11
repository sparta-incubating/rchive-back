package kr.sparta.rchive.domain.core.service;

import java.util.List;
import kr.sparta.rchive.domain.user.dto.request.RoleRequestReq;
import kr.sparta.rchive.domain.user.dto.response.RoleGetLastSelectRoleRes;
import kr.sparta.rchive.domain.user.entity.Role;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.TrackRoleEnum;
import kr.sparta.rchive.domain.user.enums.UserRoleEnum;
import kr.sparta.rchive.domain.user.exception.RoleCustomException;
import kr.sparta.rchive.domain.user.exception.RoleExceptionCode;
import kr.sparta.rchive.domain.user.service.RoleService;
import kr.sparta.rchive.domain.user.service.TrackService;
import kr.sparta.rchive.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
        if(user.getUserRole() == UserRoleEnum.USER){
            throw new RoleCustomException(RoleExceptionCode.FORBIDDEN_USER_ROLE_USER);
        }

        List<Role> roleList = roleService.findAllByUserIdApprove(user.getId());
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

        Track track = role.getTrack();

        RoleGetLastSelectRoleRes res = RoleGetLastSelectRoleRes.builder()
                .trackRole(role.getTrackRole())
                .trackName(track.getTrackName())
                .period(track.getPeriod())
                .build();

        return res;
    }

}
