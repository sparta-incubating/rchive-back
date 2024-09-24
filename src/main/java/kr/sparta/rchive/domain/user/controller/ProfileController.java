package kr.sparta.rchive.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import kr.sparta.rchive.domain.bookmark.service.BookmarkService;
import kr.sparta.rchive.domain.comment.dto.response.CommentProfileRes;
import kr.sparta.rchive.domain.comment.service.CommentService;
import kr.sparta.rchive.domain.core.service.UserTrackRoleCoreService;
import kr.sparta.rchive.domain.post.dto.response.PostGetRes;
import kr.sparta.rchive.domain.user.dto.request.ProfileUpdateNicknameReq;
import kr.sparta.rchive.domain.user.dto.request.ProfileUpdatePasswordReq;
import kr.sparta.rchive.domain.user.dto.request.ProfileUpdatePhoneReq;
import kr.sparta.rchive.domain.user.dto.request.ProfileUpdateProfileImgReq;
import kr.sparta.rchive.domain.user.dto.response.UserRes;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.domain.user.exception.statement.profile.UpdatePasswordException;
import kr.sparta.rchive.domain.user.exception.statement.profile.UpdateProfileException;
import kr.sparta.rchive.domain.user.response.ProfileResponseCode;
import kr.sparta.rchive.domain.user.service.UserService;
import kr.sparta.rchive.global.execption.ApiExceptionCodeExample;
import kr.sparta.rchive.global.response.CommonResponseDto;
import kr.sparta.rchive.global.security.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apis/v1/profile")
@Tag(name = "3. Profile API", description = "Profile 관련 API 입니다.")
public class ProfileController {

    private final UserService userService;
    private final UserTrackRoleCoreService userTrackRoleCoreService;
    private final BookmarkService bookmarkService;
    private final CommentService commentService;

    @GetMapping("/comment")
    @Operation(operationId = "PROFILE-001", summary = "내가 작성한 댓글 조회")
    public ResponseEntity<CommonResponseDto> getComment(
            @LoginUser User user
    ) {
        List<CommentProfileRes> responseList = commentService.findCommentList(user);

        return ResponseEntity.status(ProfileResponseCode.OK_GET_COMMENT.getHttpStatus())
                .body(CommonResponseDto.of(ProfileResponseCode.OK_GET_COMMENT, responseList));
    }

    @GetMapping("/bookmark")
    @Operation(operationId = "PROFILE-002", summary = "북마크 목록 조회")
    public ResponseEntity<CommonResponseDto> getBookmark(
            @LoginUser User user
    ) {
        List<PostGetRes> responseList = bookmarkService.getUserBookmark(user.getId());

        return ResponseEntity.status(ProfileResponseCode.OK_GET_BOOKMARK.getHttpStatus())
                .body(CommonResponseDto.of(ProfileResponseCode.OK_GET_BOOKMARK, responseList));
    }

    @GetMapping
    @Operation(operationId = "PROFILE-005", summary = "프로필 조회")
    public ResponseEntity<CommonResponseDto> updatePassword(
            @LoginUser User user,
            @RequestParam("trackName") TrackNameEnum trackName,
            @RequestParam("period") Integer period) {
        UserRes res = userTrackRoleCoreService.getProfileUserPage(user, trackName, period);

        return ResponseEntity.status(ProfileResponseCode.OK_GET_PROFILE.getHttpStatus())
                .body(CommonResponseDto.of(ProfileResponseCode.OK_GET_PROFILE, res));
    }

    @PatchMapping("/nickname")
    @Operation(operationId = "PROFILE-006", summary = "닉네임 변경")
    @ApiExceptionCodeExample(UpdateProfileException.class)
    public ResponseEntity<CommonResponseDto> updateNickname(
            @LoginUser User user,
            @Valid @RequestBody ProfileUpdateNicknameReq req) {
        userService.updateNickname(user, req);

        return ResponseEntity.status(ProfileResponseCode.OK_UPDATE_NICKNAME.getHttpStatus())
                .body(CommonResponseDto.of(ProfileResponseCode.OK_UPDATE_NICKNAME, null));
    }

    @PatchMapping("/password")
    @Operation(operationId = "PROFILE-007", summary = "비밀번호 변경")
    @ApiExceptionCodeExample(UpdatePasswordException.class)
    public ResponseEntity<CommonResponseDto> updatePassword(
            @LoginUser User user,
            @Valid @RequestBody ProfileUpdatePasswordReq req) {
        userService.updatePassword(user, req);

        return ResponseEntity.status(ProfileResponseCode.OK_UPDATE_PASSWORD.getHttpStatus())
                .body(CommonResponseDto.of(ProfileResponseCode.OK_UPDATE_PASSWORD, null));
    }

    @PatchMapping("/phone")
    @Operation(operationId = "PROFILE-008", summary = "휴대폰 번호 변경")
    public ResponseEntity<CommonResponseDto> updatePhone(
            @LoginUser User user,
            @Valid @RequestBody ProfileUpdatePhoneReq req) {
        userService.updatePhone(user, req);

        return ResponseEntity.status(ProfileResponseCode.OK_UPDATE_PHONE.getHttpStatus())
                .body(CommonResponseDto.of(ProfileResponseCode.OK_UPDATE_PHONE, null));
    }

    @PatchMapping("/img")
    @Operation(operationId = "PROFILE-009", summary = "프로필 이미지 변경")
    @ApiExceptionCodeExample(UpdateProfileException.class)
    public ResponseEntity<CommonResponseDto> updateProfileImg(
            @LoginUser User user,
            @RequestBody ProfileUpdateProfileImgReq req) {
        userService.updateProfileImg(user, req);

        return ResponseEntity.status(ProfileResponseCode.OK_UPDATE_PROFILE_IMG.getHttpStatus())
                .body(CommonResponseDto.of(ProfileResponseCode.OK_UPDATE_PROFILE_IMG, null));
    }

    @GetMapping("/bookmark/search")
    @Operation(operationId = "PROFILE-010", summary = "북마크 목록 검색")
    public ResponseEntity<CommonResponseDto> searchBookmark(
            @LoginUser User user,
            @RequestParam("keyword") String keyword
    ) {
        List<PostGetRes> responseList = bookmarkService.searchBookmark(user, keyword);

        return ResponseEntity.status(ProfileResponseCode.OK_SEARCH_BOOKMARK.getHttpStatus())
                .body(CommonResponseDto.of(ProfileResponseCode.OK_SEARCH_BOOKMARK, responseList));
    }
}
