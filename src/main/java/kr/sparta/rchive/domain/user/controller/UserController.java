package kr.sparta.rchive.domain.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import kr.sparta.rchive.domain.user.dto.request.UserSignupReq;
import kr.sparta.rchive.domain.user.response.UserResponseCode;
import kr.sparta.rchive.domain.user.service.UserService;
import kr.sparta.rchive.global.response.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "1. User API", description = "User 관련 API 입니다.")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDto> signup(UserSignupReq req){
        userService.signup(req);
        return ResponseEntity.status(UserResponseCode.OK_SIGNUP.getHttpStatus())
                .body(CommonResponseDto.of(UserResponseCode.OK_SIGNUP, null));

    }
}
