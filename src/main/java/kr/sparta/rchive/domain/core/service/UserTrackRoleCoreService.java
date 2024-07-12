package kr.sparta.rchive.domain.core.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;
import kr.sparta.rchive.domain.user.dto.request.RoleRequestReq;
import kr.sparta.rchive.domain.user.dto.response.RoleGetLastSelectRoleRes;
import kr.sparta.rchive.domain.user.dto.response.UserRes;
import kr.sparta.rchive.domain.user.entity.Role;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
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

}
