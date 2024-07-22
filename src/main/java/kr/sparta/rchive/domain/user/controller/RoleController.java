package kr.sparta.rchive.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.sparta.rchive.domain.core.service.UserTrackRoleCoreService;
import kr.sparta.rchive.domain.user.dto.request.RoleRequestReq;
import kr.sparta.rchive.domain.user.dto.request.RoleSelectRoleReq;
import kr.sparta.rchive.domain.user.dto.response.RoleGetLastSelectRoleRes;
import kr.sparta.rchive.domain.user.dto.response.RoleGetTrackNameListRes;
import kr.sparta.rchive.domain.user.dto.response.RoleGetTrackPeriodListRes;
import kr.sparta.rchive.domain.user.dto.response.RoleRes;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.AuthEnum;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.domain.user.response.RoleResponseCode;
import kr.sparta.rchive.domain.user.service.RoleService;
import kr.sparta.rchive.domain.user.service.TrackService;
import kr.sparta.rchive.global.response.CommonResponseDto;
import kr.sparta.rchive.global.security.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/role")
@Tag(name = "2. Role API", description = "Role 관련 API 입니다.")
public class RoleController {

    private final UserTrackRoleCoreService userTrackRoleCoreService;
    private final RoleService roleService;
    private final TrackService trackService;

    @GetMapping
    @Operation(operationId = "ROLE-001", summary = "내 권한(트랙 및 기수) 조회")
    public ResponseEntity<CommonResponseDto> getMyRoleList(@LoginUser User user) {
        List<RoleRes> resList = userTrackRoleCoreService.getMyRoleList(user);

        return ResponseEntity.status(RoleResponseCode.OK_GET_MY_ROLE_LIST.getHttpStatus())
                .body(CommonResponseDto.of(RoleResponseCode.OK_GET_MY_ROLE_LIST, resList));
    }

    @PostMapping
    @Operation(operationId = "ROLE-002", summary = "내 권한(트랙 및 기수) 요청")
    public ResponseEntity<CommonResponseDto> requestRole(@LoginUser User user,
            @RequestBody RoleRequestReq req) {
        userTrackRoleCoreService.requestRole(user, req);

        return ResponseEntity.status(RoleResponseCode.OK_REQUEST_ROLE.getHttpStatus())
                .body(CommonResponseDto.of(RoleResponseCode.OK_REQUEST_ROLE, null));
    }

    @PatchMapping("/select")
    @Operation(operationId = "ROLE-003", summary = "내 권한(트랙 및 기수) 선택")
    public ResponseEntity<CommonResponseDto> selectRole(@LoginUser User user,
            @RequestBody RoleSelectRoleReq req) {
        userTrackRoleCoreService.selectRole(user, req);

        return ResponseEntity.status(RoleResponseCode.OK_SELECT_ROLE.getHttpStatus())
                .body(CommonResponseDto.of(RoleResponseCode.OK_SELECT_ROLE, null));
    }

    @GetMapping("/track")
    @Operation(operationId = "ROLE-004", summary = "트랙명 조회")
    public ResponseEntity<CommonResponseDto> getTrackNameList() {
        RoleGetTrackNameListRes res = trackService.getTrackNameList();

        return ResponseEntity.status(RoleResponseCode.OK_GET_TRACK_NAME.getHttpStatus())
                .body(CommonResponseDto.of(RoleResponseCode.OK_GET_TRACK_NAME, res));
    }

    @GetMapping("/track/period")
    @Operation(operationId = "ROLE-005", summary = "트랙의 기수 조회")
    public ResponseEntity<CommonResponseDto> getTrackPeriodList(
            @RequestParam TrackNameEnum trackName) {
        RoleGetTrackPeriodListRes res = trackService.getTrackPeriodList(trackName);

        return ResponseEntity.status(RoleResponseCode.OK_GET_TRACK_PERIOD.getHttpStatus())
                .body(CommonResponseDto.of(RoleResponseCode.OK_GET_TRACK_PERIOD, res));
    }

    @GetMapping("/result")
    @Operation(operationId = "ROLE-006", summary = "권한 신청 결과 조회 - 최초 로그인")
    public ResponseEntity<CommonResponseDto> getResultRoleFirstLogin(@LoginUser User user) {
        AuthEnum auth = roleService.getResultRoleFirstLogin(user);

        return ResponseEntity.status(
                        RoleResponseCode.OK_GET_RESULT_ROLE_FIRST_LOGIN.getHttpStatus())
                .body(CommonResponseDto.of(RoleResponseCode.OK_GET_RESULT_ROLE_FIRST_LOGIN, auth));
    }

    @GetMapping("/request")
    @Operation(operationId = "ROLE-007", summary = "권한 신청 여부 조회 - 최초 로그인")
    public ResponseEntity<CommonResponseDto> getRequestRoleFirstLogin(@LoginUser User user) {
        boolean isRequest = roleService.getRequestRoleFirstLogin(user);

        return ResponseEntity.status(
                        RoleResponseCode.OK_GET_REQUEST_ROLE_FIRST_LOGIN.getHttpStatus())
                .body(CommonResponseDto.of(RoleResponseCode.OK_GET_REQUEST_ROLE_FIRST_LOGIN,
                        isRequest));
    }


}
