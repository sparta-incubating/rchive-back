package kr.sparta.rchive.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.sparta.rchive.domain.core.service.UserTrackRoleCoreService;
import kr.sparta.rchive.domain.user.dto.request.RoleRequestReq;
import kr.sparta.rchive.domain.user.dto.request.UserSignupReq;
import kr.sparta.rchive.domain.user.response.UserResponseCode;
import kr.sparta.rchive.domain.user.service.RoleService;
import kr.sparta.rchive.global.response.CommonResponseDto;
import kr.sparta.rchive.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/role")
@Tag(name = "2. Role API", description = "Role 관련 API 입니다.")
public class RoleController {

    private final UserTrackRoleCoreService userTrackRoleCoreService;
    private final RoleService roleService;

    @PostMapping
    @Operation(operationId = "ROLE-002", summary = "내 권한(트랙 및 기수) 요청")
    public ResponseEntity<CommonResponseDto> requestRole(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody RoleRequestReq req){
        userTrackRoleCoreService.requestRole(userDetails.getUser(), req);

        return ResponseEntity.status(UserResponseCode.OK_REQUEST_ROLE.getHttpStatus())
                .body(CommonResponseDto.of(UserResponseCode.OK_REQUEST_ROLE, null));
    }
}