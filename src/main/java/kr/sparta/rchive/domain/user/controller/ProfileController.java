package kr.sparta.rchive.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.sparta.rchive.domain.core.service.UserTrackRoleCoreService;
import kr.sparta.rchive.domain.user.dto.response.RoleGetLastSelectRoleRes;
import kr.sparta.rchive.domain.user.dto.response.UserRes;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.response.ProfileResponseCode;
import kr.sparta.rchive.domain.user.response.RoleResponseCode;
import kr.sparta.rchive.domain.user.service.UserService;
import kr.sparta.rchive.global.response.CommonResponseDto;
import kr.sparta.rchive.global.security.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
@Tag(name = "3. Profile API", description = "Profile 관련 API 입니다.")
public class ProfileController {

    private final UserService userService;
    private final UserTrackRoleCoreService userTrackRoleCoreService;

    @GetMapping("/backoffice")
    @Operation(operationId = "PROFILE-010", summary = "프로필 조회 - 백오피스")
    public ResponseEntity<CommonResponseDto> getProfile(
            @LoginUser User user
    ){
        UserRes res = userTrackRoleCoreService.getProfile(user);

        return ResponseEntity.status(ProfileResponseCode.OK_GET_PROFILE_BACKOFFICE.getHttpStatus())
                .body(CommonResponseDto.of(ProfileResponseCode.OK_GET_PROFILE_BACKOFFICE, res));
    }
}
