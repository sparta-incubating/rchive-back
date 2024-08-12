package kr.sparta.rchive.domain.post.dto.request;

import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import lombok.Builder;

@Builder
public record RecentSearchKeywordReq(
        TrackNameEnum trackName,
        Integer period,
        String keyword
) {
}
