package kr.sparta.rchive.domain.user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import kr.sparta.rchive.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Tag(name = "1. User API", description = "User 관련 API 입니다.")
public class UserController {
    private final UserService userService;
}
