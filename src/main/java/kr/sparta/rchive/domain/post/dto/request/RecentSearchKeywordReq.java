package kr.sparta.rchive.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import lombok.Builder;

@Builder
public record RecentSearchKeywordReq(
        @Schema(description = "트랙 이름", example = "ANDROID")
        TrackNameEnum trackName,
        @Schema(description = "트랙 기수", example = "1")
        Integer period,
        @Schema(description = "검색 키워드", example = "keyword")
        String keyword
) {
}
