package kr.sparta.rchive.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.sparta.rchive.domain.comment.dto.request.CommentCreateReq;
import kr.sparta.rchive.domain.comment.service.CommentService;
import kr.sparta.rchive.domain.core.service.PostCommentCoreService;
import kr.sparta.rchive.domain.core.service.PostBookmarkCoreService;
import kr.sparta.rchive.domain.core.service.PostTagCoreService;
import kr.sparta.rchive.domain.post.dto.request.PostCreateReq;
import kr.sparta.rchive.domain.post.dto.request.PostOpenCloseReq;
import kr.sparta.rchive.domain.post.dto.request.PostUpdateReq;
import kr.sparta.rchive.domain.post.dto.request.TagCreateReq;
import kr.sparta.rchive.domain.post.dto.response.*;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import kr.sparta.rchive.domain.post.response.PostResponseCode;
import kr.sparta.rchive.domain.post.service.TagService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@Tag(name = "4. Post API", description = "Post 관련 API입니다.")
public class PostController {

    private final PostTagCoreService postTagCoreService;
    private final PostBookmarkCoreService postBookmarkCoreService;
    private final PostCommentCoreService postCommentCoreService;
    private final TagService tagService;
    private final CommentService commentService;

    @PostMapping
    @Operation(operationId = "POST-001", summary = "게시물 생성")
    public ResponseEntity<CommonResponseDto> createPost(
            @LoginUser User user,
            @RequestParam("trackName") TrackNameEnum trackName,
            @RequestParam("loginPeriod") Integer period,
            @RequestBody PostCreateReq request
    ) {
        PostCreateRes response = postTagCoreService.createPost(user, trackName, period, request);

        return ResponseEntity.status(PostResponseCode.OK_CREATE_POST.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_CREATE_POST, response));
    }

    @PatchMapping("/{postId}")
    @Operation(operationId = "POST-002", summary = "게시물 관리 - 수정")
    public ResponseEntity<CommonResponseDto> updatePost(
            @LoginUser User user,
            @RequestParam("trackName") TrackNameEnum trackName,
            @RequestParam("loginPeriod") Integer period,
            @PathVariable Long postId,
            @RequestBody PostUpdateReq request
    ) {
        PostModifyRes response = postTagCoreService.updatePost(user, trackName, period, postId, request);

        return ResponseEntity.status(PostResponseCode.OK_UPDATE_POST.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_UPDATE_POST, response));
    }

    @DeleteMapping("/{postId}")
    @Operation(operationId = "POST-003", summary = "게시물 관리 - 삭제")
    public ResponseEntity<CommonResponseDto> deletePost(
            @LoginUser User user,
            @RequestParam("trackName") TrackNameEnum trackName,
            @RequestParam("loginPeriod") Integer period,
            @PathVariable Long postId
    ) {
        postTagCoreService.deletePost(user, trackName, period, postId);

        return ResponseEntity.status(PostResponseCode.OK_DELETE_POST.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_DELETE_POST, null));
    }

    @GetMapping("/search")
    @Operation(operationId = "POST-004", summary = "게시물 검색")
    public ResponseEntity<CommonResponseDto> searchPosts(
            @LoginUser User user,
            @RequestParam("trackName") TrackNameEnum trackName,
            @RequestParam("loginPeriod") Integer period,
            @RequestParam(value = "category", required = false) PostTypeEnum postType,
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "tutorId", required = false) Long tutorId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = new CustomPageable(page, size, Sort.unsorted());
        Page<PostSearchRes> responseList = postTagCoreService.searchPosts(user, postType, trackName, period, keyword, tutorId, pageable);

        return ResponseEntity.status(PostResponseCode.OK_SEARCH_POST.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_SEARCH_POST, responseList));
    }

    @GetMapping("/category")
    @Operation(operationId = "POST-005", summary = "게시물 목록 조회")
    public ResponseEntity<CommonResponseDto> getPostCategory(
            @LoginUser User user,
            @RequestParam("trackName") TrackNameEnum trackName,
            @RequestParam("loginPeriod") Integer period,
            @RequestParam("category") PostTypeEnum postType,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = new CustomPageable(page, size, Sort.unsorted());
        Page<PostGetRes> responseList =
                postTagCoreService.getPostListByCategory(user, trackName, period, postType, pageable);
        return ResponseEntity.status(PostResponseCode.OK_GET_CATEGORY_POST.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_GET_CATEGORY_POST, responseList));
    }

    @GetMapping("/{postId}")
    @Operation(operationId = "POST-006", summary = "게시물 단건 조회")
    public ResponseEntity<CommonResponseDto> getPost(
            @LoginUser User user,
            @PathVariable Long postId,
            @RequestParam("trackName") TrackNameEnum trackName,
            @RequestParam("period") Integer period
    ) {
        PostGetSinglePostRes response = postTagCoreService.getPost(user, postId, trackName, period);

        return ResponseEntity.status(PostResponseCode.OK_GET_SINGLE_POST.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_GET_SINGLE_POST, response));
    }

    @PostMapping("/{postId}/comments")
    @Operation(operationId = "POST-007", summary = "게시물 댓글 작성")
    public ResponseEntity<CommonResponseDto> createComment(
            @LoginUser User user,
            @PathVariable Long postId,
            @RequestParam(value = "parentComment", required = false) Long parentCommentId,
            @RequestBody CommentCreateReq request
    ) {
        postCommentCoreService.createComment(user, postId, parentCommentId, request);

        return ResponseEntity.status(PostResponseCode.OK_CREATE_COMMENT.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_CREATE_COMMENT, null));
    }

    @DeleteMapping("/comment/{commentId}")
    @Operation(operationId = "POST-008", summary = "게시물 댓글 삭제")
    public ResponseEntity<CommonResponseDto> deleteComment(
            @LoginUser User user,
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(user, commentId);

        return ResponseEntity.status(PostResponseCode.OK_DELETE_COMMENT.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_DELETE_COMMENT, null));
    }

    @PostMapping("/tags")
    @Operation(operationId = "POST-010", summary = "사용할 태그 생성")
    public ResponseEntity<CommonResponseDto> createTag(
            @LoginUser User user,
            @RequestBody TagCreateReq request
    ) {
        TagCreateRes response = tagService.createTag(request.tagName());

        return ResponseEntity.status(PostResponseCode.OK_CREATE_TAG.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_CREATE_TAG, response));
    }

    @GetMapping("/tags")
    @Operation(operationId = "POST-011", summary = "사용할 태그 검색")
    public ResponseEntity<CommonResponseDto> searchTag(
            @LoginUser User user,
            @RequestParam("tagName") String tagName
    ) {

        List<TagSearchRes> responseList = tagService.searchTag(tagName);

        return ResponseEntity.status(PostResponseCode.OK_SEARCH_TAG.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_SEARCH_TAG, responseList));
    }

    @GetMapping("/tags/search")
    @Operation(operationId = "POST-012", summary = "태그를 클릭하여 검색하는 기능")
    public ResponseEntity<CommonResponseDto> searchPostByTag(
            @LoginUser User user,
            @RequestParam("trackName") TrackNameEnum trackName,
            @RequestParam("loginPeriod") Integer period,
            @RequestParam("tagId") Long tagId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = new CustomPageable(page, size, Sort.unsorted());
        Page<PostGetRes> responseList = postTagCoreService
                .searchPostByTag(trackName, period, tagId, user, pageable);
        return ResponseEntity.status(PostResponseCode.OK_SEARCH_POST_BY_TAG.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_SEARCH_POST_BY_TAG, responseList));
    }

    @PostMapping("/{postId}/bookmark")
    @Operation(operationId = "POST-013", summary = "북마크 생성")
    public ResponseEntity<CommonResponseDto> createBookmark(
            @LoginUser User user,
            @PathVariable Long postId
    ) {
        postBookmarkCoreService.createBookmark(user, postId);

        return ResponseEntity.status(PostResponseCode.OK_CREATE_BOOKMARK.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_CREATE_BOOKMARK, null));
    }

    @DeleteMapping("/{postId}/bookmark")
    @Operation(operationId = "POST-014", summary = "북마크 삭제")
    public ResponseEntity<CommonResponseDto> deleteBookmark(
            @LoginUser User user,
            @PathVariable Long postId
    ) {
        postBookmarkCoreService.deleteBookmark(user, postId);

        return ResponseEntity.status(PostResponseCode.OK_DELETE_BOOKMARK.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_DELETE_BOOKMARK, null));
    }

    @PatchMapping("/open")
    @Operation(operationId = "POST-015", summary = "게시물 공개 여부 변경 - 공개")
    public ResponseEntity<CommonResponseDto> openPost(
            @LoginUser User user,
            @RequestParam("trackName") TrackNameEnum trackName,
            @RequestParam("loginPeriod") Integer period,
            @RequestBody PostOpenCloseReq request
    ) {
        postTagCoreService.openPost(user, trackName, period, request.postIdList());

        return ResponseEntity.status(PostResponseCode.OK_OPEN_POST.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_OPEN_POST, null));
    }

    @PatchMapping("/close")
    @Operation(operationId = "POST-016", summary = "게시물 공개 여부 변경 - 비공개")
    public ResponseEntity<CommonResponseDto> closePost(
            @LoginUser User user,
            @RequestParam("trackName") TrackNameEnum trackName,
            @RequestParam("loginPeriod") Integer period,
            @RequestBody PostOpenCloseReq request
    ) {
        postTagCoreService.closePost(user, trackName, period, request.postIdList());

        return ResponseEntity.status(PostResponseCode.OK_CLOSE_POST.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_CLOSE_POST, null));
    }

    @GetMapping("/tutors")
    @Operation(operationId = "POST-017", summary = "튜터를 검색")
    public ResponseEntity<CommonResponseDto> searchTutor(
            @LoginUser User user,
            @RequestParam("trackName") TrackNameEnum trackName,
            @RequestParam("loginPeriod") Integer period,
            @RequestParam("inputPeriod") Integer inputPeriod,
            @RequestParam(value = "tutorName", required = false) String tutorName
    ) {
        List<TutorRes> responseList = postTagCoreService.searchTutor(user, trackName, period, inputPeriod, tutorName);

        return ResponseEntity.status(PostResponseCode.OK_SEARCH_TUTOR.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_SEARCH_TUTOR, responseList));
    }
}