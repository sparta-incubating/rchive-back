package kr.sparta.rchive.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.sparta.rchive.domain.core.service.EducationDataTagCoreService;
import kr.sparta.rchive.domain.post.dto.request.TagCreateReq;
import kr.sparta.rchive.domain.post.dto.request.TagSearchReq;
import kr.sparta.rchive.domain.post.dto.response.PostSearchByTagRes;
import kr.sparta.rchive.domain.post.dto.response.TagCreateRes;
import kr.sparta.rchive.domain.post.dto.response.TagSearchRes;
import kr.sparta.rchive.domain.post.response.PostResponseCode;
import kr.sparta.rchive.domain.post.service.EducationDataService;
import kr.sparta.rchive.domain.post.service.TagService;
import kr.sparta.rchive.global.response.CommonResponseDto;
import kr.sparta.rchive.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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

    private final EducationDataService educationDataService;
    private final EducationDataTagCoreService educationDataTagCoreService;
    private final TagService tagService;

    @GetMapping("/tags")
    @Operation(operationId = "POST-010", summary = "사용할 태그 검색")
    public ResponseEntity<CommonResponseDto> searchTag(
            @RequestBody TagSearchReq request
    ) {

        List<TagSearchRes> responseList = tagService.searchTag(request.tagName());

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
            @RequestParam("tagName") String tagName
    ) {
        List<PostSearchByTagRes> responseList = educationDataTagCoreService
                .searchPostByTag(tagName, userDetails.getUser());

        return ResponseEntity.status(PostResponseCode.OK_SEARCH_POST_BY_TAG.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_SEARCH_POST_BY_TAG, responseList));
    }

}
