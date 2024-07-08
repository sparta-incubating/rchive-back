package kr.sparta.rchive.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.sparta.rchive.domain.core.service.PostTagCoreService;
import kr.sparta.rchive.domain.post.dto.request.PostCreateReq;
//import kr.sparta.rchive.domain.post.dto.request.PostModifyReq;
import kr.sparta.rchive.domain.post.dto.request.TagCreateReq;
import kr.sparta.rchive.domain.post.dto.request.TagSearchReq;
import kr.sparta.rchive.domain.post.dto.response.PostCreateRes;
//import kr.sparta.rchive.domain.post.dto.response.PostModifyRes;
import kr.sparta.rchive.domain.post.dto.response.PostSearchByTagRes;
import kr.sparta.rchive.domain.post.dto.response.TagCreateRes;
import kr.sparta.rchive.domain.post.dto.response.TagSearchRes;
import kr.sparta.rchive.domain.post.response.PostResponseCode;
import kr.sparta.rchive.domain.post.service.PostService;
import kr.sparta.rchive.domain.post.service.TagService;
import kr.sparta.rchive.domain.user.entity.User;
import kr.sparta.rchive.global.custom.CustomPageable;
import kr.sparta.rchive.global.response.CommonResponseDto;
import kr.sparta.rchive.global.security.LoginUser;
import kr.sparta.rchive.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

//    @PatchMapping("/{postId}")
//    @Operation(operationId = "POST-002", summary = "게시물 관리 - 수정")
//    public ResponseEntity<CommonResponseDto> modifyPost(
//            @PathVariable Long postId,
//            @RequestBody PostModifyReq request
//    ) {
//        PostModifyRes response = postTagCoreService.modifyPost(postId, request);
//
//        return ResponseEntity.status(PostResponseCode.OK_MODIFY_POST.getHttpStatus())
//                .body(CommonResponseDto.of(PostResponseCode.OK_MODIFY_POST, response));
//    }

    @DeleteMapping("/{postId}")
    @Operation(operationId = "POST-003", summary = "게시물 관리 - 삭제")
    public ResponseEntity<CommonResponseDto> deletePost(
            @PathVariable Long postId
    ) {
        postService.deletePost(postId);

        return ResponseEntity.status(PostResponseCode.OK_DELETE_POST.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_DELETE_POST, null));
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

    @PostMapping("/tags")
    @Operation(operationId = "POST-009", summary = "사용할 태그 생성")
    public ResponseEntity<CommonResponseDto> createTag(
            @RequestBody TagCreateReq request
    ){
        TagCreateRes response = tagService.createTag(request.tagName());

        return ResponseEntity.status(PostResponseCode.OK_CREATE_TAG.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_CREATE_TAG, response));
    }

    @GetMapping("/tags/search")
    @Operation(operationId = "POST-011", summary = "태그를 이용하여 검색하는 기능")
    public ResponseEntity<CommonResponseDto> searchPostByTag(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam("tagName") String tagName,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = new CustomPageable(page, size, Sort.unsorted());

        Page<PostSearchByTagRes> responseList = postTagCoreService
                .searchPostByTag(tagName, userDetails.getUser(), pageable);

        return ResponseEntity.status(PostResponseCode.OK_SEARCH_POST_BY_TAG.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_SEARCH_POST_BY_TAG, responseList));
    }

}