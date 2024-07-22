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
        @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail
    ) {
        String thumbnailImageUrl = s3ImageService.getUrlAfterUpload(thumbnail);
        return ResponseEntity.status(GlobalResponseCode.FILE_UPLOAD.getHttpStatus())
                .body(CommonResponseDto.of(GlobalResponseCode.FILE_UPLOAD, thumbnailImageUrl));
    }

    @GetMapping("/thumnail/delete")
    @Operation(operationId = "S3-002", summary = "썸네일 파일 삭제")
    public ResponseEntity<CommonResponseDto> deleteThumbnail(
            @RequestParam(value = "thumbnailUrl") String thumbnailUrl
    ) {
        s3ImageService.deleteS3Image(thumbnailUrl);

        return ResponseEntity.status(GlobalResponseCode.FILE_DELETE.getHttpStatus())
                .body(CommonResponseDto.of(GlobalResponseCode.FILE_DELETE, null));
    }
}
