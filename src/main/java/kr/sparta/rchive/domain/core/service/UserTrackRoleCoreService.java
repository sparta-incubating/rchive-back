package kr.sparta.rchive.domain.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kr.sparta.rchive.domain.user.dto.request.RoleRequestListReq;
import kr.sparta.rchive.domain.user.dto.request.RoleRequestReq;
import kr.sparta.rchive.domain.user.dto.request.RoleReq;
import kr.sparta.rchive.domain.user.dto.response.RoleGetLastSelectRoleRes;
import kr.sparta.rchive.domain.user.dto.response.RoleGetTrackRoleRequestCountRes;
import kr.sparta.rchive.domain.user.dto.response.RoleGetTrackRoleRequestListRes;
import kr.sparta.rchive.domain.user.dto.response.RoleRes;
import kr.sparta.rchive.domain.user.dto.response.UserRes;
import kr.sparta.rchive.domain.user.entity.Role;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.AuthEnum;
import kr.sparta.rchive.domain.user.enums.OrderRoleListEnum;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.domain.user.enums.TrackRoleEnum;
import kr.sparta.rchive.domain.user.exception.RoleCustomException;
import kr.sparta.rchive.domain.user.exception.RoleExceptionCode;
import kr.sparta.rchive.domain.user.service.RoleService;
import kr.sparta.rchive.domain.user.service.TrackService;
import kr.sparta.rchive.domain.user.service.UserService;
import kr.sparta.rchive.global.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserTrackRoleCoreService {

    private final UserService userService;
    private final RoleService roleService;
    private final TrackService trackService;
    private final RedisService redisService;

    public List<RoleRes> getMyRoleList(User user) {
        List<Role> roleList = roleService.findRoleListByUserIdAuthApprove(user.getId());

        for (Role r : roleList) {
            if (r.getTrackRole() == TrackRoleEnum.PM) {
                TrackNameEnum trackName = r.getTrack().getTrackName();
                List<Track> trackList = trackService.findTrackListByTrackName(trackName);

                return trackList.stream()
                        .map(track -> {
                            return RoleRes.builder()
                                    .trackId(track.getId())
                                    .trackRoleEnum(TrackRoleEnum.PM)
                                    .trackName(track.getTrackName())
                                    .period(track.getPeriod())
                                    .build();
                        }).collect(Collectors.toList());
            }
        }

        return roleList.stream()
                .map(role -> {
                    return RoleRes.builder()
                            .trackId(role.getTrack().getId())
                            .trackRoleEnum(role.getTrackRole())
                            .trackName(role.getTrack().getTrackName())
                            .period(role.getTrack().getPeriod())
                            .build();
                }).collect(Collectors.toList());
    }

    public void selectRole(User user, RoleReq req) {
        if (req.period() == 0) {
            throw new RoleCustomException(RoleExceptionCode.BAD_REQUEST_NO_PARAMETER_PERIOD);
        }

        Track track = trackService.findTrackByTrackNameAndPeriod(req.trackName(), req.period());
        boolean isPm = roleService.existByUserAndTrackByPm(user.getId(), track.getTrackName());

        if (!isPm) {
            Role role = roleService.findRoleByUserIdAndTrackId(user.getId(), track.getId());
            if (role == null || role.getAuth() != AuthEnum.APPROVE) {
                throw new RoleCustomException(RoleExceptionCode.BAD_REQUEST_NO_ROLE);
            }
        }

        redisService.setLastSelectRole(user, track.getId());
    }

    public Page<RoleGetTrackRoleRequestListRes> getUserTrackRoleRequestList(
            User user, OrderRoleListEnum sort, TrackNameEnum trackName, Integer loginPeriod,
            AuthEnum status, Integer searchPeriod, String searchKeyword,
            TrackRoleEnum searchTrackRole, Pageable pageable) {

        Track managerTrack = trackService.findTrackByTrackNameAndPeriod(trackName, loginPeriod);
        if (loginPeriod == 0) {
            roleService.existByUserAndTrackByPmThrowException(user.getId(), trackName);
        } else {
            roleService.existByUserAndTrackByApmThrowException(user.getId(), managerTrack.getId());
        }

        List<Role> roleList;
        if (status == null) {
            roleList = roleService.findRoleListInBackOfficeAuthNoReject(
                    managerTrack, searchPeriod, searchKeyword, searchTrackRole, sort);
        } else {
            roleList = roleService.findRoleListInBackOffice(
                    managerTrack, searchPeriod, status, searchKeyword, searchTrackRole, sort);
        }

        List<RoleGetTrackRoleRequestListRes> responseList = roleList.stream()
                .map(role -> {
                    return RoleGetTrackRoleRequestListRes.builder()
                            .username(role.getUser().getUsername())
                            .trackRole(role.getTrackRole())
                            .period(role.getTrack().getPeriod())
                            .email(role.getUser().getEmail())
                            .createdAt(role.getCreatedAt().toLocalDate())
                            .auth(role.getAuth())
                            .build();
                }).collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), responseList.size());

        return new PageImpl<>(responseList.subList(start, end), pageable, responseList.size());
    }

    public void requestRole(User user, RoleRequestReq req) {
        Track track = trackService.findTrackByTrackNameAndPeriod(req.trackName(), req.period());
        roleService.requestRole(user, track, req.trackRole());
    }

    public RoleGetLastSelectRoleRes getLastSelectRoleUserPage(User user) {

        Long trackId = redisService.getLastSelectRole(user);
        if (trackId == null) {
            List<Role> roleList = roleService.findRoleListByUserIdAuthApprove(user.getId());
            if (roleList.size() == 1) {
                redisService.setLastSelectRole(user, roleList.get(0).getTrack().getId());
                trackId = roleList.get(0).getTrack().getId();
            } else {
                throw new RoleCustomException(RoleExceptionCode.NOT_FOUND_LAST_SELECT_ROLE);
            }
        }

        Role role = roleService.findRoleByUserIdAndTrackId(user.getId(), trackId);
        Track track = role.getTrack();

        if (role.getAuth() != AuthEnum.APPROVE) {
            redisService.deleteLastSelectRole(user);
            throw new RoleCustomException(RoleExceptionCode.NOT_FOUND_LAST_SELECT_ROLE);
        }

        return RoleGetLastSelectRoleRes.builder()
                .trackId(track.getId())
                .trackRole(role.getTrackRole())
                .trackName(track.getTrackName())
                .period(track.getPeriod())
                .build();
    }

    public RoleGetLastSelectRoleRes getLastSelectRoleBackoffice(User user) {

        Role role = roleService.getRoleByManager(user);
        Track track = role.getTrack();

        return RoleGetLastSelectRoleRes.builder()
                .trackId(track.getId())
                .trackRole(role.getTrackRole())
                .trackName(track.getTrackName())
                .period(track.getPeriod())
                .build();
    }

    public UserRes getProfileUserPage(User user, TrackNameEnum trackName, Integer period) {

        Track track = trackService.findTrackByTrackNameAndPeriod(trackName, period);
        Role role = roleService.findRoleByUserIdAndTrackId(user.getId(), track.getId());

        return UserRes.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .profileImg(user.getProfileImg())
                .nickname(user.getNickname())
                .birth(user.getBirth())
                .phone(user.getPhone())
                .trackRole(role.getTrackRole())
                .trackName(track.getTrackName())
                .period(track.getPeriod())
                .build();
    }

    public UserRes getProfileBackoffice(User user) {

        Role role = roleService.getRoleByManager(user);
        Track track = role.getTrack();

        return UserRes.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .profileImg(user.getProfileImg())
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
        userService.approveApmUpdateUserRole(reqList);
        roleService.deleteRoleListByAuthNotApprove(reqList);
        roleService.deleteApmRoleListExceptLastApprove(reqList);
    }

    @Transactional
    public void userTrackRoleReject(User manager, List<RoleRequestListReq> reqList) {
        Track managaerTrack = roleService.getRoleByManager(manager).getTrack();
        managerTrackRoleInvalid(managaerTrack, reqList);

        roleService.rejectRoleRequest(reqList);
    }

    public void managerTrackRoleInvalid(Track managaerTrack, List<RoleRequestListReq> reqList) {
        /* PM */
        if (managaerTrack.getPeriod() == 0) {
            for (RoleRequestListReq req : reqList) {
                if (req.trackName() != managaerTrack.getTrackName()) {
                    throw new RoleCustomException(RoleExceptionCode.FORBIDDEN_ROLE);
                }
            }
        }

        /* APM */
        else {
            for (RoleRequestListReq req : reqList) {
                if (req.trackName() != managaerTrack.getTrackName()
                        || req.period() != managaerTrack.getPeriod()) {
                    throw new RoleCustomException(RoleExceptionCode.FORBIDDEN_ROLE);
                }
            }
        }
    }

    public RoleGetTrackRoleRequestCountRes getTrackRoleRequestCount(
            User user, TrackNameEnum trackName, Integer loginPeriod, Integer searchPeriod) {

        if (loginPeriod == 0) {
            roleService.existByUserAndTrackByPmThrowException(user.getId(), trackName);
        } else {
            Track track = trackService.findTrackByTrackNameAndPeriod(trackName, loginPeriod);
            roleService.existByUserAndTrackByApmThrowException(user.getId(), track.getId());
        }

        if (loginPeriod == 0) {
            return RoleGetTrackRoleRequestCountRes.builder()
                    .statusAll(roleService.countByRoleRequestByPm(trackName, searchPeriod, null))
                    .statusWait(roleService.countByRoleRequestByPm(trackName, searchPeriod,
                            AuthEnum.WAIT))
                    .statusApprove(roleService.countByRoleRequestByPm(trackName, searchPeriod,
                            AuthEnum.APPROVE))
                    .build();
        }

        return RoleGetTrackRoleRequestCountRes.builder()
                .statusAll(roleService.countByRoleRequestByApm(trackName, loginPeriod, null))
                .statusWait(roleService.countByRoleRequestByApm(trackName, loginPeriod,
                        AuthEnum.WAIT))
                .statusApprove(roleService.countByRoleRequestByApm(trackName, loginPeriod,
                        AuthEnum.APPROVE))
                .build();

    }

    @Transactional
    public void trackPermission(User user, TrackNameEnum trackName, Integer period, Long trackId) {
        if (period != 0) {
            throw new RoleCustomException(RoleExceptionCode.FORBIDDEN_ROLE);
        }

        Track managerTrack = trackService.findTrackByTrackNameAndPeriod(trackName, period);

        roleService.existByUserAndTrackByPmThrowException(user.getId(),
                managerTrack.getTrackName());

        trackService.trackPermissionTrue(trackId);
    }

    @Transactional
    public void trackRejection(User user, TrackNameEnum trackName, Integer period, Long trackId) {
        if (period != 0) {
            throw new RoleCustomException(RoleExceptionCode.FORBIDDEN_ROLE);
        }

        Track managerTrack = trackService.findTrackByTrackNameAndPeriod(trackName, period);

        roleService.existByUserAndTrackByPmThrowException(user.getId(),
                managerTrack.getTrackName());

        trackService.trackRejection(trackId);
    }
}
