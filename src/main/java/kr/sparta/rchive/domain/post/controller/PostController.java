package kr.sparta.rchive.domain.post.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.sparta.rchive.domain.core.service.EducationDataTagCoreService;
import kr.sparta.rchive.domain.post.dto.request.TagSearchReq;
import kr.sparta.rchive.domain.post.dto.response.TagSearchRes;
import kr.sparta.rchive.domain.post.response.PostResponseCode;
import kr.sparta.rchive.domain.post.service.EducationDataService;
import kr.sparta.rchive.domain.post.service.TagService;
import kr.sparta.rchive.global.response.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    @Operation(summary = "사용할 태그 검색", description = "게시물에 등록할 태그가 존재하는지 검색")
    public ResponseEntity<CommonResponseDto> searchTag(
            @RequestBody TagSearchReq request
    ) {

        List<TagSearchRes> responseList = tagService.searchTag(request.tag());

        return ResponseEntity.status(PostResponseCode.OK_SEARCH_TAG.getHttpStatus())
                .body(CommonResponseDto.of(PostResponseCode.OK_SEARCH_TAG, responseList));
    }
}
