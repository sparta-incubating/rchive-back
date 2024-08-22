package kr.sparta.rchive.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import kr.sparta.rchive.domain.user.dto.request.AuthPhoneReq;
import kr.sparta.rchive.domain.user.dto.request.AuthPhoneValidReq;
import kr.sparta.rchive.domain.user.dto.request.UserFindEmailReq;
import kr.sparta.rchive.domain.user.dto.request.UserFindPasswordReq;
import kr.sparta.rchive.domain.user.dto.request.UserFindPasswordUpdateReq;
import kr.sparta.rchive.domain.user.dto.request.UserSignupReq;
import kr.sparta.rchive.domain.user.dto.response.FindEmailRes;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.exception.statement.user.ReissueException;
import kr.sparta.rchive.domain.user.response.UserResponseCode;
import kr.sparta.rchive.domain.user.service.UserService;
import kr.sparta.rchive.global.execption.ApiExceptionCodeExample;
import kr.sparta.rchive.domain.user.exception.statement.user.SignupException;
import kr.sparta.rchive.global.response.CommonResponseDto;
import kr.sparta.rchive.global.security.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apis/v1/users")
@Tag(name = "1. User API", description = "User 관련 API 입니다.")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    @Operation(operationId = "USER-001", summary = "회원가입")
    @ApiExceptionCodeExample(SignupException.class)
    public ResponseEntity<CommonResponseDto> signup(
            @Valid @RequestBody UserSignupReq req
    ) {
        userService.signup(req);
        return ResponseEntity.status(UserResponseCode.OK_SIGNUP.getHttpStatus())
                .body(CommonResponseDto.of(UserResponseCode.OK_SIGNUP, null));
    }

    @DeleteMapping("/logout")
    @Operation(operationId = "USER-004", summary = "로그아웃")
    public ResponseEntity<CommonResponseDto> logout(HttpServletResponse res,
            @LoginUser User user)
            throws UnsupportedEncodingException {
        userService.logout(res, user);
        return ResponseEntity.status(UserResponseCode.OK_LOGOUT.getHttpStatus())
                .body(CommonResponseDto.of(UserResponseCode.OK_LOGOUT, null));
    }

    @PostMapping("/reissue")
    @Operation(operationId = "USER-005", summary = "토큰 재발급")
    @ApiExceptionCodeExample(ReissueException.class)
    public ResponseEntity<CommonResponseDto> reissue(HttpServletRequest req,
            HttpServletResponse res)
            throws UnsupportedEncodingException, ParseException {
        userService.reissue(req, res);
        return ResponseEntity.status(UserResponseCode.OK_REISSUE.getHttpStatus())
                .body(CommonResponseDto.of(UserResponseCode.OK_REISSUE, null));
    }

    @DeleteMapping
    @Operation(operationId = "USER-006", summary = "회원 탈퇴")
    public ResponseEntity<CommonResponseDto> withdraw(HttpServletResponse res,
            @LoginUser User user)
            throws UnsupportedEncodingException {
        userService.logout(res, user);
        userService.withdraw(user);
        return ResponseEntity.status(UserResponseCode.OK_DELETE_USER.getHttpStatus())
                .body(CommonResponseDto.of(UserResponseCode.OK_DELETE_USER, null));
    }

    @GetMapping("overlap/email")
    @Operation(operationId = "USER-007", summary = "이메일 중복 여부 조회")
    public ResponseEntity<CommonResponseDto> overlapEmail(@RequestParam("email") String email) {
        boolean isOverlap = userService.overlapEmail(email);

        if (isOverlap) {
            return ResponseEntity.status(UserResponseCode.OK_OVERLAP_EMAIL.getHttpStatus())
                    .body(CommonResponseDto.of(UserResponseCode.OK_OVERLAP_EMAIL, true));
        } else {
            return ResponseEntity.status(UserResponseCode.OK_OVERLAP_EMAIL.getHttpStatus())
                    .body(CommonResponseDto.of(UserResponseCode.OK_OVERLAP_EMAIL, false));
        }
    }

    @GetMapping("/overlap/nickname")
    @Operation(operationId = "USER-008", summary = "닉네임 중복 여부 조회")
    public ResponseEntity<CommonResponseDto> withdraw(@RequestParam("nickname") String nickname) {
        boolean isOverlap = userService.overlapNickname(nickname);

        if (isOverlap) {
            return ResponseEntity.status(UserResponseCode.OK_OVERLAP_NICKNAME.getHttpStatus())
                    .body(CommonResponseDto.of(UserResponseCode.OK_OVERLAP_NICKNAME, true));
        } else {
            return ResponseEntity.status(UserResponseCode.OK_OVERLAP_NICKNAME.getHttpStatus())
                    .body(CommonResponseDto.of(UserResponseCode.OK_OVERLAP_NICKNAME, false));
        }
    }

    @GetMapping("/token/expired")
    @Operation(operationId = "USER-009", summary = "유저 토큰 만료 여부 조회")
    public ResponseEntity<CommonResponseDto> tokenExpired(
            HttpServletRequest req
    ) throws UnsupportedEncodingException, ParseException {
        Boolean isExpired = userService.tokenExpired(req);

        return ResponseEntity.status(UserResponseCode.OK_ACCESS_TOKEN_IS_EXPIRED.getHttpStatus())
                .body(CommonResponseDto.of(UserResponseCode.OK_ACCESS_TOKEN_IS_EXPIRED, isExpired));
    }

    @PostMapping("/auth/phone/send")
    @Operation(operationId = "USER-010", summary = "휴대폰 인증 전송")
    public ResponseEntity<CommonResponseDto> sendAuthPhone(
            @Valid @RequestBody AuthPhoneReq req) {
        userService.sendAuthPhone(req);

        return ResponseEntity.status(UserResponseCode.OK_SEND_AUTH_PHONE.getHttpStatus())
                .body(CommonResponseDto.of(UserResponseCode.OK_SEND_AUTH_PHONE, null));
    }

    @PostMapping("/auth/phone/valid")
    @Operation(operationId = "USER-011", summary = "휴대폰 인증 확인")
    public ResponseEntity<CommonResponseDto> validAuthPhone(
            @Valid @RequestBody AuthPhoneValidReq req) {
        userService.validAuthPhone(req);

        return ResponseEntity.status(UserResponseCode.OK_VALID_AUTH_PHONE.getHttpStatus())
                .body(CommonResponseDto.of(UserResponseCode.OK_VALID_AUTH_PHONE, null));
    }

    @PostMapping("/find/email")
    @Operation(operationId = "USER-012", summary = "이메일 찾기")
    public ResponseEntity<CommonResponseDto> findUserEmail(
            @Valid @RequestBody UserFindEmailReq req) {
        List<FindEmailRes> res = userService.findUserEmail(req);

        return ResponseEntity.status(UserResponseCode.OK_FIND_EMAIL.getHttpStatus())
                .body(CommonResponseDto.of(UserResponseCode.OK_FIND_EMAIL, res));
    }

    @PostMapping("/find/password")
    @Operation(operationId = "USER-013", summary = "비밀번호 찾기")
    public ResponseEntity<CommonResponseDto> findUserPassword(
            @Valid @RequestBody UserFindPasswordReq req) {
        userService.findUserPassword(req);

        return ResponseEntity.status(UserResponseCode.OK_FIND_PASSWORD.getHttpStatus())
                .body(CommonResponseDto.of(UserResponseCode.OK_FIND_PASSWORD, null));
    }

    @PatchMapping("/find/password")
    @Operation(operationId = "USER-014", summary = "비밀번호 찾기 후 변경")
    public ResponseEntity<CommonResponseDto> findUserPasswordUpdate(
            @Valid @RequestBody UserFindPasswordUpdateReq req) {
        userService.findUserPasswordUpdate(req);

        return ResponseEntity.status(UserResponseCode.OK_FIND_PASSWORD_UPDATE.getHttpStatus())
                .body(CommonResponseDto.of(UserResponseCode.OK_FIND_PASSWORD_UPDATE, null));
    }
}
