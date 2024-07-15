package kr.sparta.rchive.domain.backoffice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.sparta.rchive.domain.backoffice.respoonse.BackofficeResponseCode;
import kr.sparta.rchive.domain.core.service.PostTagCoreService;
import kr.sparta.rchive.domain.core.service.UserTrackRoleCoreService;
import kr.sparta.rchive.domain.user.dto.request.RoleRequestListReq;
import kr.sparta.rchive.domain.post.dto.response.PostSearchBackOfficeRes;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import kr.sparta.rchive.domain.user.dto.response.RoleGetLastSelectRoleRes;
import kr.sparta.rchive.domain.user.dto.response.UserRes;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.global.custom.CustomPageable;
import kr.sparta.rchive.global.response.CommonResponseDto;
import kr.sparta.rchive.global.security.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/backoffice")
@Tag(name = "5. BackOffice API", description = "BackOffice 관련 API 입니다.")
public class BackofficeController {

    private final UserTrackRoleCoreService userTrackRoleCoreService;
    private final PostTagCoreService postTagCoreService;

    @GetMapping("/post/search")
    @Operation(operationId = "BACKOFFICE-007", summary = "백오피스에서 교육자료 검색")
    public ResponseEntity<CommonResponseDto> searchPostInBackOffice(
            @LoginUser User user,
            @RequestParam("trackName") TrackNameEnum trackName,
            @RequestParam(value = "postType", required = false) PostTypeEnum postType,
            @RequestParam(value = "uploadedAt", required = false) LocalDate uploadedAt,
            @RequestParam(value = "period", required = false) Integer period,
            @RequestParam(value = "tutor", required = false) String tutor,
            @RequestParam(value = "isOpened", required = false) Boolean isOpened,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = new CustomPageable(page, size, Sort.by("uploadedAt").descending());
        Page<PostSearchBackOfficeRes> responseList =
                postTagCoreService.getPostListInBackOffice(user, trackName, postType, uploadedAt,
                        period, tutor, isOpened, pageable);
        return ResponseEntity.status(BackofficeResponseCode.OK_SEARCH_POST_LIST.getHttpStatus())
                .body(CommonResponseDto.of(BackofficeResponseCode.OK_SEARCH_POST_LIST, responseList));
    }

    @PostMapping("/role/approve")
    @Operation(operationId = "BACKOFFICE-002", summary = "유저 권한 수락 - 백오피스")
    public ResponseEntity<CommonResponseDto> userTrackRoleApprove(
            @LoginUser User user,
            @RequestBody List<RoleRequestListReq> reqList
    ){
        userTrackRoleCoreService.userTrackRoleApprove(user,reqList);

        return ResponseEntity.status(BackofficeResponseCode.OK_APPROVE_USER_TRACK_ROLE.getHttpStatus())
                .body(CommonResponseDto.of(BackofficeResponseCode.OK_APPROVE_USER_TRACK_ROLE, null));
    }

    @GetMapping("/role/select")
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
    public ResponseEntity<CommonResponseDto> getProfile(
            @LoginUser User user
    ) {
        UserRes res = userTrackRoleCoreService.getProfile(user);

        return ResponseEntity.status(BackofficeResponseCode.OK_GET_PROFILE.getHttpStatus())
                .body(CommonResponseDto.of(BackofficeResponseCode.OK_GET_PROFILE, res));
    }
}
