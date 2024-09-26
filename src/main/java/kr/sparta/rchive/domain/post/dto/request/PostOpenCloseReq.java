package kr.sparta.rchive.domain.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

@Builder
public record PostOpenCloseReq(
        @Schema(description = "게시물 ID 리스트", example = "[1,2,3]")
        List<Long> postIdList
) {
}
