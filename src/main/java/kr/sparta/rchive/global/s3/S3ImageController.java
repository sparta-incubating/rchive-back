package kr.sparta.rchive.global.s3;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.sparta.rchive.global.response.CommonResponseDto;
import kr.sparta.rchive.global.response.GlobalResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/s3")
@Tag(name = "6. S3 API", description = "S3 관련 API입니다.")
public class S3ImageController {

    private final S3ImageService s3ImageService;

    @PostMapping(value = "/thumbnail/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(operationId = "S3-001", summary = "썸네일 파일 업로드")
    public ResponseEntity<CommonResponseDto> uploadThumbnail(
        @RequestPart(value = "thumbnail", required = false) final MultipartFile thumbnail
    ) {
        String thumbnailImageUrl = s3ImageService.getUrlAfterThumbnailUpload(thumbnail);
        return ResponseEntity.status(GlobalResponseCode.UPLOAD.getHttpStatus())
                .body(CommonResponseDto.of(GlobalResponseCode.UPLOAD, thumbnailImageUrl));
    }
}
