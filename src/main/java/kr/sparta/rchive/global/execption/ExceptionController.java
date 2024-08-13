package kr.sparta.rchive.global.execption;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.sparta.rchive.domain.post.exception.PostExceptionCode;
import kr.sparta.rchive.domain.user.exception.RoleExceptionCode;
import kr.sparta.rchive.domain.user.exception.TrackExceptionCode;
import kr.sparta.rchive.domain.user.exception.UserExceptionCode;
import kr.sparta.rchive.global.s3.exception.S3ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apis/v1/exceptions")
@Tag(name = "0. Exception API", description = "Exception 관련 API 입니다.")
public class ExceptionController {

    @GetMapping("/global")
    @Operation(operationId = "EXCEPTION-GLOBAL", summary = "Global 관련 Exception 입니다.")
    @ApiExceptionCodeExample(GlobalExceptionCode.class)
    public void getGlobalExceptionCode() {
    }

    @GetMapping("/user")
    @Operation(operationId = "EXCEPTION-USER", summary = "User 관련 Exception 입니다.")
    @ApiExceptionCodeExample(UserExceptionCode.class)
    public void getUserExceptionCode() {
    }

    @GetMapping("/role")
    @Operation(operationId = "EXCEPTION-ROLE", summary = "Role 관련 Exception 입니다.")
    @ApiExceptionCodeExample(RoleExceptionCode.class)
    public void getRoleExceptionCode() {
    }

    @GetMapping("/track")
    @Operation(operationId = "EXCEPTION-TRACK", summary = "Track 관련 Exception 입니다.")
    @ApiExceptionCodeExample(TrackExceptionCode.class)
    public void getTrackExceptionCode() {
    }

    @GetMapping("/post")
    @Operation(operationId = "EXCEPTION-POST", summary = "Track 관련 Exception 입니다.")
    @ApiExceptionCodeExample(PostExceptionCode.class)
    public void getPostExceptionCode() {
    }

    @GetMapping("/s3")
    @Operation(operationId = "EXCEPTION-S3", summary = "S3 관련 Exception 입니다.")
    @ApiExceptionCodeExample(S3ExceptionCode.class)
    public void getS3ExceptionCode() {
    }
}
