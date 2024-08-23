package kr.sparta.rchive.domain.post.dto.request;

import lombok.Builder;

import java.util.List;

@Builder
public record PostOpenCloseReq(
        List<Long> postIdList
) {
}
