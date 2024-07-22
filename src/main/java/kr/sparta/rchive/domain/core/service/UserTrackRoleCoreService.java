package kr.sparta.rchive.domain.core.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kr.sparta.rchive.domain.user.dto.request.RoleRequestListReq;
import kr.sparta.rchive.domain.user.dto.request.RoleRequestReq;
import kr.sparta.rchive.domain.user.dto.response.RoleGetLastSelectRoleRes;
import kr.sparta.rchive.domain.user.dto.response.RoleGetTrackRoleRequestCountRes;
import kr.sparta.rchive.domain.user.dto.response.RoleGetTrackRoleRequestListRes;
import kr.sparta.rchive.domain.user.dto.response.RoleRes;
import kr.sparta.rchive.domain.user.dto.response.UserRes;
import kr.sparta.rchive.domain.user.entity.Role;
import kr.sparta.rchive.domain.user.entity.Track;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.AuthEnum;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.domain.user.enums.TrackRoleEnum;
import kr.sparta.rchive.domain.user.exception.RoleCustomException;
import kr.sparta.rchive.domain.user.exception.RoleExceptionCode;
import kr.sparta.rchive.domain.user.service.RoleService;
import kr.sparta.rchive.domain.user.service.TrackService;
import kr.sparta.rchive.domain.user.service.UserService;
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

    public List<RoleRes> getMyRoleList(User user) {
        List<Role> roleList = roleService.findAllByUserIdApprove(user.getId());

        TrackNameEnum trackName = null;
        for (Role r : roleList) {
            if (r.getTrackRole() == TrackRoleEnum.PM) {
                trackName = r.getTrack().getTrackName();
            }
        }

        if (trackName != null) {
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

    public Page<RoleGetTrackRoleRequestListRes> getUserTrackRoleRequestList(
            User user, TrackNameEnum trackName, Integer period, AuthEnum status,
            Integer searchPeriod, String email, TrackRoleEnum trackRole, Pageable pageable) {

        Track managerTrack = trackService.findTrackByTrackNameAndPeriod(trackName, period);
        if (period == 0) {
            roleService.existByUserAndTrackByPm(user.getId(), trackName);
        } else {
            roleService.existByUserAndTrackByApm(user.getId(), managerTrack.getId());
        }

        List<Role> roleList = new ArrayList<>();
        if (status == null) {
            roleList = roleService.findRoleListInBackOfficeAuthNoReject(
                    managerTrack, searchPeriod, email, trackRole);
        } else {
            roleList = roleService.findRoleListInBackOffice(
                    managerTrack, searchPeriod, status, email, trackRole);
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

    public RoleGetLastSelectRoleRes getLastSelectRoleBackoffice(User user) {

        Role role = roleService.getRoleByManager(user);
        Track track = role.getTrack();

        return RoleGetLastSelectRoleRes.builder()
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
        if (managaerTrack.getPeriod() == 0) {
            for (RoleRequestListReq req : reqList) {
                if (req.trackName() != managaerTrack.getTrackName()) {
                    throw new RoleCustomException(RoleExceptionCode.FORBIDDEN_TRACK_NOT_ACCESS);
                }
            }
        }

        /* APM */
        else {
            for (RoleRequestListReq req : reqList) {
                if (req.trackName() != managaerTrack.getTrackName()
                        || req.period() != managaerTrack.getPeriod()) {
                    throw new RoleCustomException(RoleExceptionCode.FORBIDDEN_TRACK_NOT_ACCESS);
                }
            }
        }
    }

    public RoleGetTrackRoleRequestCountRes getTrackRoleRequestCount(
            User user, TrackNameEnum trackName, Integer period, Integer searchPeriod) {

        if (period == 0) {
            roleService.existByUserAndTrackByPm(user.getId(), trackName);
        } else {
            Track track = trackService.findTrackByTrackNameAndPeriod(trackName, period);
            roleService.existByUserAndTrackByApm(user.getId(), track.getId());
        }

        if (period == 0) {
            return RoleGetTrackRoleRequestCountRes.builder()
                    .statusAll(roleService.countByRoleRequestByPm(trackName, searchPeriod, null))
                    .statusWait(roleService.countByRoleRequestByPm(trackName, searchPeriod,
                            AuthEnum.WAIT))
                    .statusApprove(roleService.countByRoleRequestByPm(trackName, searchPeriod,
                            AuthEnum.APPROVE))
                    .build();
        }

        return RoleGetTrackRoleRequestCountRes.builder()
                .statusAll(roleService.countByRoleRequestByApm(trackName, period, null))
                .statusWait(roleService.countByRoleRequestByApm(trackName, period,
                        AuthEnum.WAIT))
                .statusApprove(roleService.countByRoleRequestByApm(trackName, period,
                        AuthEnum.APPROVE))
                .build();

    }


}
