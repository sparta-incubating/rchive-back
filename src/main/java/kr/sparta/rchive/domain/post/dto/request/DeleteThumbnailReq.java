package kr.sparta.rchive.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import lombok.Builder;

@Builder
public record DeleteThumbnailReq(
        @Schema(description = "트랙 이름", example = "Android")
        TrackNameEnum trackName,
        @Schema(description = "기수", example = "1")
        Integer period
) {

}
