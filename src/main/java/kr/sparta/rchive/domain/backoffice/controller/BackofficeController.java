package kr.sparta.rchive.domain.backoffice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.sparta.rchive.domain.backoffice.respoonse.BackofficeResponseCode;
import kr.sparta.rchive.domain.core.service.UserTrackRoleCoreService;
import kr.sparta.rchive.domain.user.dto.request.RoleRequestListReq;
import kr.sparta.rchive.domain.user.dto.response.RoleGetLastSelectRoleRes;
import kr.sparta.rchive.domain.user.dto.response.RoleGetTrackRoleRequestCountRes;
import kr.sparta.rchive.domain.user.dto.response.UserRes;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.domain.user.response.ProfileResponseCode;
import kr.sparta.rchive.domain.user.response.RoleResponseCode;
import kr.sparta.rchive.domain.user.service.RoleService;
import kr.sparta.rchive.domain.user.service.TrackService;
import kr.sparta.rchive.global.response.CommonResponseDto;
import kr.sparta.rchive.global.security.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/backoffice")
@Tag(name = "5. BackOffice API", description = "BackOffice 관련 API 입니다.")
public class BackofficeController {

    private final UserTrackRoleCoreService userTrackRoleCoreService;

    @PostMapping("/role/approve")
    @Operation(operationId = "BACKOFFICE-002", summary = "유저의 트랙 권한 수락 - 백오피스")
    public ResponseEntity<CommonResponseDto> userTrackRoleApprove(
            @LoginUser User user,
            @RequestBody List<RoleRequestListReq> reqList
    ){
        userTrackRoleCoreService.userTrackRoleApprove(user,reqList);

        return ResponseEntity.status(BackofficeResponseCode.OK_APPROVE_USER_TRACK_ROLE.getHttpStatus())
                .body(CommonResponseDto.of(BackofficeResponseCode.OK_APPROVE_USER_TRACK_ROLE, null));
    }

    @DeleteMapping("/role/reject")
    @Operation(operationId = "BACKOFFICE-003", summary = "유저의 트랙 권한 거절 - 백오피스")
    public ResponseEntity<CommonResponseDto> userTrackRoleReject(
            @LoginUser User user,
            @RequestBody List<RoleRequestListReq> reqList
    ){
        userTrackRoleCoreService.userTrackRoleReject(user,reqList);

        return ResponseEntity.status(BackofficeResponseCode.OK_REJECT_USER_TRACK_ROLE.getHttpStatus())
                .body(CommonResponseDto.of(BackofficeResponseCode.OK_REJECT_USER_TRACK_ROLE, null));
    }

    @GetMapping("/role/count")
    @Operation(operationId = "BACKOFFICE-004", summary = "유저의 트랙 권한 신청 건수 - 백오피스")
    public ResponseEntity<CommonResponseDto> getTrackRoleRequestCount (
            @LoginUser User user,
            @RequestParam("trackName") TrackNameEnum trackName,
            @RequestParam(required = false, value = "period") Integer period
    ){
        RoleGetTrackRoleRequestCountRes res = userTrackRoleCoreService.getTrackRoleRequestCount(user,trackName,period);

        return ResponseEntity.status(BackofficeResponseCode.OK_GET_USER_TRACK_ROLE_REQUEST_COUNT.getHttpStatus())
                .body(CommonResponseDto.of(BackofficeResponseCode.OK_GET_USER_TRACK_ROLE_REQUEST_COUNT, res));
    }

    @GetMapping("/role/select")
    @Operation(operationId = "BACKOFFICE-007", summary = "마지막에 선택한 권한 조회 - 백오피스")
    public ResponseEntity<CommonResponseDto> getLastSelectRoleBackoffice(
            @LoginUser User user
    ){
        RoleGetLastSelectRoleRes res = userTrackRoleCoreService.getLastSelectRoleBackoffice(user);

        return ResponseEntity.status(BackofficeResponseCode.OK_GET_LAST_SELECT_ROLE.getHttpStatus())
                .body(CommonResponseDto.of(BackofficeResponseCode.OK_GET_LAST_SELECT_ROLE, res));
    }

    @GetMapping("/profile")
    @Operation(operationId = "BACKOFFICE-008", summary = "프로필 조회 - 백오피스")
    public ResponseEntity<CommonResponseDto> getProfileBackoffice(
            @LoginUser User user
    ){
        UserRes res = userTrackRoleCoreService.getProfileBackoffice(user);

        return ResponseEntity.status(BackofficeResponseCode.OK_GET_PROFILE.getHttpStatus())
                .body(CommonResponseDto.of(BackofficeResponseCode.OK_GET_PROFILE, res));
    }
}
