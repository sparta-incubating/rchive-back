package kr.sparta.rchive.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.sparta.rchive.domain.core.service.PostTagCoreService;
import kr.sparta.rchive.domain.post.dto.request.PostCreateReq;
import kr.sparta.rchive.domain.post.dto.request.PostModifyReq;
import kr.sparta.rchive.domain.post.dto.request.TagCreateReq;
import kr.sparta.rchive.domain.post.dto.response.*;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import kr.sparta.rchive.domain.post.response.PostResponseCode;
import kr.sparta.rchive.domain.post.service.PostService;
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
@Tag(name = "2. Post API", description = "Post 관련 API입니다.")
public class PostController {

    private final PostService postService;
    private final PostTagCoreService postTagCoreService;
    private final TagService tagService;

    @PostMapping
    @Operation(operationId = "POST-001", summary = "게시물 생성")
    public ResponseEntity<CommonResponseDto> createPost(
            @RequestBody PostCreateReq request
    ) {
        PostCreateRes response = postTagCoreService.createPost(request);

        return ResponseEntity.status(PostResponseCode.OK_CREATE_POST.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_CREATE_POST, response));
    }

    @PatchMapping("/{postId}")
    @Operation(operationId = "POST-002", summary = "게시물 관리 - 수정")
    public ResponseEntity<CommonResponseDto> updatePost(
            @PathVariable Long postId,
            @RequestBody PostModifyReq request
    ) {
        PostModifyRes response = postTagCoreService.updatePost(postId, request);

        return ResponseEntity.status(PostResponseCode.OK_MODIFY_POST.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_MODIFY_POST, response));
    }

    @DeleteMapping("/{postId}")
    @Operation(operationId = "POST-003", summary = "게시물 관리 - 삭제")
    public ResponseEntity<CommonResponseDto> deletePost(
            @PathVariable Long postId
    ) {
        postService.deletePost(postId);

        return ResponseEntity.status(PostResponseCode.OK_DELETE_POST.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_DELETE_POST, null));
    }

    @GetMapping("/category")
    @Operation(operationId = "POST-005", summary = "게시물 목록 조회")
    public ResponseEntity<CommonResponseDto> getPostCategory(
            @LoginUser User user,
            @RequestParam("trackName") TrackNameEnum trackName,
            @RequestParam("period") Integer period,
            @RequestParam("category") PostTypeEnum postType
    ){
        List<PostGetCategoryPostRes> responseList =
                postTagCoreService.getPostListByCategory(user, trackName, period, postType);

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

    @PostMapping("/tags")
    @Operation(operationId = "POST-009", summary = "사용할 태그 생성")
    public ResponseEntity<CommonResponseDto> createTag(
            @RequestBody TagCreateReq request
    ) {
        TagCreateRes response = tagService.createTag(request.tagName());

        return ResponseEntity.status(PostResponseCode.OK_CREATE_TAG.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_CREATE_TAG, response));
    }

    @GetMapping("/tags")
    @Operation(operationId = "POST-010", summary = "사용할 태그 검색")
    public ResponseEntity<CommonResponseDto> searchTag(
            @RequestParam("tagName") String tagName
    ) {

        List<TagSearchRes> responseList = tagService.searchTag(tagName);

        return ResponseEntity.status(PostResponseCode.OK_SEARCH_TAG.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_SEARCH_TAG, responseList));
    }

    @GetMapping("/tags/search")
    @Operation(operationId = "POST-011", summary = "태그를 이용하여 검색하는 기능")
    public ResponseEntity<CommonResponseDto> searchPostByTag(
            @LoginUser User user,
            @RequestParam("tagName") String tagName,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = new CustomPageable(page, size, Sort.unsorted());

        Page<PostSearchByTagRes> responseList = postTagCoreService
                .searchPostByTag(tagName, user, pageable);

        return ResponseEntity.status(PostResponseCode.OK_SEARCH_POST_BY_TAG.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_SEARCH_POST_BY_TAG, responseList));
    }

}