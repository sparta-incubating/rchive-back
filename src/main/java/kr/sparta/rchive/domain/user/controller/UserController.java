package kr.sparta.rchive.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import kr.sparta.rchive.domain.user.dto.request.UserSignupReq;
import kr.sparta.rchive.domain.user.response.UserResponseCode;
import kr.sparta.rchive.domain.user.service.UserService;
import kr.sparta.rchive.global.response.CommonResponseDto;
import kr.sparta.rchive.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "1. User API", description = "User 관련 API 입니다.")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    @Operation(operationId = "USER-001", summary = "회원가입")
    public ResponseEntity<CommonResponseDto> signup(@RequestBody UserSignupReq req){
        userService.signup(req);
        return ResponseEntity.status(UserResponseCode.OK_SIGNUP.getHttpStatus())
                .body(CommonResponseDto.of(UserResponseCode.OK_SIGNUP, null));
    }

    @DeleteMapping("/logout")
    @Operation(operationId = "USER-004", summary = "로그아웃")
    public ResponseEntity<CommonResponseDto> logout(HttpServletResponse res,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
            throws UnsupportedEncodingException {
        userService.logout(res, userDetails.getUser());
        return ResponseEntity.status(UserResponseCode.OK_LOGOUT.getHttpStatus())
                .body(CommonResponseDto.of(UserResponseCode.OK_LOGOUT,null));
    }

    @PostMapping("/reissue")
    @Operation(operationId = "USER-005", summary = "토큰 재발급")
    public ResponseEntity<CommonResponseDto> reissue(HttpServletRequest req, HttpServletResponse res)
            throws UnsupportedEncodingException, ParseException {
        userService.reissue(req,res);
        return ResponseEntity.status(UserResponseCode.OK_REISSUE.getHttpStatus())
                .body(CommonResponseDto.of(UserResponseCode.OK_REISSUE,null));
    }

    @DeleteMapping
    @Operation(operationId = "USER-006", summary = "회원 탈퇴")
    public ResponseEntity<CommonResponseDto> withdraw(HttpServletResponse res,
            @AuthenticationPrincipal UserDetailsImpl userDetails)
            throws UnsupportedEncodingException {
        userService.logout(res, userDetails.getUser());
        userService.withdraw(userDetails.getUser());
        return ResponseEntity.status(UserResponseCode.OK_DELETE_USER.getHttpStatus())
                .body(CommonResponseDto.of(UserResponseCode.OK_DELETE_USER,null));
    }

    @GetMapping("overlap/email")
    @Operation(operationId = "USER-009", summary = "이메일 중복 여부 조회")
    public ResponseEntity<CommonResponseDto> overlapEmail(@RequestParam("email") String email){
        boolean isOverlap = userService.overlapEmail(email);

        if(isOverlap){
            return ResponseEntity.status(UserResponseCode.OK_OVERLAP_EMAIL.getHttpStatus())
                    .body(CommonResponseDto.of(UserResponseCode.OK_OVERLAP_EMAIL, true));
        }else{
            return ResponseEntity.status(UserResponseCode.OK_OVERLAP_EMAIL.getHttpStatus())
                    .body(CommonResponseDto.of(UserResponseCode.OK_OVERLAP_EMAIL, false));
        }
    }
  
    @GetMapping("/overlap/nickname")
    @Operation(operationId = "USER-010", summary = "닉네임 중복 여부 조회")
    public ResponseEntity<CommonResponseDto> withdraw(@RequestParam("nickname") String nickname){
        boolean isOverlap = userService.overlapNickname(nickname);

        if(isOverlap){
            return ResponseEntity.status(UserResponseCode.OK_OVERLAP_NICKNAME.getHttpStatus())
                    .body(CommonResponseDto.of(UserResponseCode.OK_OVERLAP_NICKNAME, true));
        }else{
            return ResponseEntity.status(UserResponseCode.OK_OVERLAP_NICKNAME.getHttpStatus())
                    .body(CommonResponseDto.of(UserResponseCode.OK_OVERLAP_NICKNAME, false));
        }
    }
}
