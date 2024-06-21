package kr.sparta.rchive.domain.post.dto.request;

import lombok.Builder;

@Builder
public record TagSearchReq(
        String tag
) {

}
