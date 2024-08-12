package kr.sparta.rchive.domain.post.dto.request;

import java.util.List;

public record PostOpenCloseReq(
        List<Long> postIdList
) {
}
