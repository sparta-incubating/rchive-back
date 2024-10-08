package kr.sparta.rchive.global.s3.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.sparta.rchive.global.execption.ApiExceptionCodeExample;
import kr.sparta.rchive.global.response.CommonResponseDto;
import kr.sparta.rchive.global.response.GlobalResponseCode;
import kr.sparta.rchive.global.s3.exception.statement.DeleteThumbnailException;
import kr.sparta.rchive.global.s3.exception.statement.UploadThumbnailException;
import kr.sparta.rchive.global.s3.response.S3ResponseCode;
import kr.sparta.rchive.global.s3.service.S3ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/apis/v1/s3")
@Tag(name = "6. S3 API", description = "S3 관련 API입니다.")
public class S3ImageController {

    private final S3ImageService s3ImageService;

    @PostMapping(value = "/thumbnail/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(operationId = "S3-001", summary = "썸네일 파일 업로드")
    @ApiExceptionCodeExample(UploadThumbnailException.class)
    public ResponseEntity<CommonResponseDto> uploadThumbnail(
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail
    ) {
        String thumbnailImageUrl = s3ImageService.getUrlAfterUpload(thumbnail);
        return ResponseEntity.status(S3ResponseCode.OK_FILE_UPLOAD.getHttpStatus())
                .body(CommonResponseDto.of(S3ResponseCode.OK_FILE_UPLOAD, thumbnailImageUrl));
    }

    @GetMapping("/thumbnail/delete")
    @Operation(operationId = "S3-002", summary = "썸네일 파일 삭제")
    @ApiExceptionCodeExample(DeleteThumbnailException.class)
    public ResponseEntity<CommonResponseDto> deleteThumbnail(
            @RequestParam(value = "thumbnailUrl") String thumbnailUrl
    ) {
        s3ImageService.deleteS3Image(thumbnailUrl);

        return ResponseEntity.status(S3ResponseCode.OK_FILE_DELETE.getHttpStatus())
                .body(CommonResponseDto.of(S3ResponseCode.OK_FILE_DELETE, null));
    }
}
