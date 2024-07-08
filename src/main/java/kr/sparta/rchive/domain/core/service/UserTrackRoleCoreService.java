package kr.sparta.rchive.domain.core.service;

import kr.sparta.rchive.domain.user.dto.request.RoleRequestReq;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.entity.User;
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
        roleService.requestRole(user,track,req.trackRole());
    }
}
