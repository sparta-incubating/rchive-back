package kr.sparta.rchive.domain.backoffice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.sparta.rchive.domain.backoffice.respoonse.BackofficeResponseCode;
import kr.sparta.rchive.domain.core.service.PostTagCoreService;
import kr.sparta.rchive.domain.core.service.UserTrackRoleCoreService;
import kr.sparta.rchive.domain.post.dto.response.PostSearchBackOfficeRes;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import kr.sparta.rchive.domain.user.dto.request.RoleRequestListReq;
import kr.sparta.rchive.domain.user.dto.response.RoleGetLastSelectRoleRes;
import kr.sparta.rchive.domain.user.dto.response.RoleGetTrackRoleRequestCountRes;
import kr.sparta.rchive.domain.user.dto.response.RoleGetTrackRoleRequestListRes;
import kr.sparta.rchive.domain.user.dto.response.UserRes;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.AuthEnum;
import kr.sparta.rchive.domain.user.enums.OrderRoleListEnum;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.domain.user.enums.TrackRoleEnum;
import kr.sparta.rchive.global.custom.CustomPageable;
import kr.sparta.rchive.global.response.CommonResponseDto;
import kr.sparta.rchive.global.security.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/backoffice")
@Tag(name = "5. BackOffice API", description = "BackOffice 관련 API 입니다.")
public class BackofficeController {

    private final UserTrackRoleCoreService userTrackRoleCoreService;
    private final PostTagCoreService postTagCoreService;

    @GetMapping("/role")
    @Operation(operationId = "BACKOFFICE-001", summary = "유저의 트랙 권한 신청 목록 조회 - 백오피스")
    public ResponseEntity<CommonResponseDto> getUserTrackRoleRequestList(
        @LoginUser User user,
        @RequestParam(value = "sort") OrderRoleListEnum sort,
        @RequestParam(value = "status", required = false) AuthEnum status,
        @RequestParam(value = "trackName") TrackNameEnum trackName,
        @RequestParam(value = "period") Integer period,
        @RequestParam(value = "searchPeriod", required = false) Integer searchPeriod,
        @RequestParam(value = "email", required = false) String email,
        @RequestParam(value = "trackRole", required = false) TrackRoleEnum trackRole,
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = new CustomPageable(page, size, Sort.unsorted());
        Page<RoleGetTrackRoleRequestListRes> responseList =
            userTrackRoleCoreService.getUserTrackRoleRequestList(user, sort, trackName, period,
                status, searchPeriod, email, trackRole, pageable);

        return ResponseEntity.status(
                BackofficeResponseCode.OK_GET_USER_TRACK_ROLE_REQUEST_LIST.getHttpStatus())
            .body(CommonResponseDto.of(
                BackofficeResponseCode.OK_GET_USER_TRACK_ROLE_REQUEST_LIST, responseList));
    }

    @PostMapping("/role/approve")
    @Operation(operationId = "BACKOFFICE-002", summary = "유저의 트랙 권한 수락 - 백오피스")
    public ResponseEntity<CommonResponseDto> userTrackRoleApprove(
        @LoginUser User user,
        @RequestBody List<RoleRequestListReq> reqList
    ) {
        userTrackRoleCoreService.userTrackRoleApprove(user, reqList);

        return ResponseEntity.status(
                BackofficeResponseCode.OK_APPROVE_USER_TRACK_ROLE.getHttpStatus())
            .body(CommonResponseDto.of(BackofficeResponseCode.OK_APPROVE_USER_TRACK_ROLE,
                null));
    }

    @DeleteMapping("/role/reject")
    @Operation(operationId = "BACKOFFICE-003", summary = "유저의 트랙 권한 거절 - 백오피스")
    public ResponseEntity<CommonResponseDto> userTrackRoleReject(
        @LoginUser User user,
        @RequestBody List<RoleRequestListReq> reqList
    ) {
        userTrackRoleCoreService.userTrackRoleReject(user, reqList);

        return ResponseEntity.status(
                BackofficeResponseCode.OK_REJECT_USER_TRACK_ROLE.getHttpStatus())
            .body(CommonResponseDto.of(BackofficeResponseCode.OK_REJECT_USER_TRACK_ROLE, null));
    }

    @GetMapping("/role/count")
    @Operation(operationId = "BACKOFFICE-004", summary = "유저의 트랙 권한 신청 건수 - 백오피스")
    public ResponseEntity<CommonResponseDto> getTrackRoleRequestCount(
        @LoginUser User user,
        @RequestParam("trackName") TrackNameEnum trackName,
        @RequestParam("period") Integer period,
        @RequestParam(value = "searchPeriod", required = false) Integer searchPeriod
    ) {
        RoleGetTrackRoleRequestCountRes res = userTrackRoleCoreService.getTrackRoleRequestCount(
            user, trackName, period, searchPeriod);

        return ResponseEntity.status(
                BackofficeResponseCode.OK_GET_USER_TRACK_ROLE_REQUEST_COUNT.getHttpStatus())
            .body(CommonResponseDto.of(
                BackofficeResponseCode.OK_GET_USER_TRACK_ROLE_REQUEST_COUNT, res));
    }

    @PatchMapping("/track/permission")
    @Operation(operationId = "BACKOFFICE-005", summary = "트랙의 일반 유저 열람 권한 수락")
    public ResponseEntity<CommonResponseDto> userTrackPermission(
        @LoginUser User user,
        @RequestParam("trackName") TrackNameEnum trackName,
        @RequestParam("loginPeriod") Integer period,
        @RequestParam("trackId") Long trackId
    ) {
        userTrackRoleCoreService.trackPermission(user, trackName, period, trackId);

        return ResponseEntity.status(BackofficeResponseCode.OK_USER_TRACK_PERMISSION.getHttpStatus())
            .body(CommonResponseDto.of(BackofficeResponseCode.OK_USER_TRACK_PERMISSION, null));
    }

    @GetMapping("/post/search")
    @Operation(operationId = "BACKOFFICE-006", summary = "백오피스에서 교육자료 검색")
    public ResponseEntity<CommonResponseDto> searchPostInBackOffice(
        @LoginUser User user,
        @RequestParam("trackName") TrackNameEnum trackName,
        @RequestParam("period") Integer period,
        @RequestParam(value = "title", required = false) String title,
        @RequestParam(value = "postType", required = false) PostTypeEnum postType,
        @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
        @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
        @RequestParam(value = "searchPeriod", required = false) Integer searchPeriod,
        @RequestParam(value = "isOpened", required = false) Boolean isOpened,
        @RequestParam(value = "tutorId", required = false) Long tutorId,
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = new CustomPageable(page, size, Sort.unsorted());
        Page<PostSearchBackOfficeRes> responseList =
            postTagCoreService.getPostListInBackOffice(user, trackName, period, title, postType,
                startDate, endDate, searchPeriod, isOpened, tutorId, pageable);
        return ResponseEntity.status(BackofficeResponseCode.OK_SEARCH_POST_LIST.getHttpStatus())
            .body(CommonResponseDto.of(BackofficeResponseCode.OK_SEARCH_POST_LIST, responseList));
    }

    @GetMapping("/role/select/last")
    @Operation(operationId = "BACKOFFICE-007", summary = "마지막에 선택한 권한 조회 - 백오피스")
    public ResponseEntity<CommonResponseDto> getLastSelectRoleBackoffice(
        @LoginUser User user
    ) {
        RoleGetLastSelectRoleRes res = userTrackRoleCoreService.getLastSelectRoleBackoffice(user);

        return ResponseEntity.status(BackofficeResponseCode.OK_GET_LAST_SELECT_ROLE.getHttpStatus())
            .body(CommonResponseDto.of(BackofficeResponseCode.OK_GET_LAST_SELECT_ROLE, res));
    }

    @GetMapping("/profile")
    @Operation(operationId = "BACKOFFICE-008", summary = "프로필 조회 - 백오피스")
    public ResponseEntity<CommonResponseDto> getProfileBackoffice(
        @LoginUser User user
    ) {
        UserRes res = userTrackRoleCoreService.getProfileBackoffice(user);

        return ResponseEntity.status(BackofficeResponseCode.OK_GET_PROFILE.getHttpStatus())
            .body(CommonResponseDto.of(BackofficeResponseCode.OK_GET_PROFILE, res));
    }

    @PatchMapping("/track/rejection")
    @Operation(operationId = "BACKOFFICE-009", summary = "트랙의 일반 유저 열람 권한 거절")
    public ResponseEntity<CommonResponseDto> userTrackRejection(
        @LoginUser User user,
        @RequestParam("trackName") TrackNameEnum trackName,
        @RequestParam("loginPeriod") Integer period,
        @RequestParam("trackId") Long trackId
    ) {
        userTrackRoleCoreService.trackRejection(user, trackName, period, trackId);

        return ResponseEntity.status(BackofficeResponseCode.OK_USER_TRACK_REJECTION.getHttpStatus())
            .body(CommonResponseDto.of(BackofficeResponseCode.OK_USER_TRACK_REJECTION, null));
    }
}
