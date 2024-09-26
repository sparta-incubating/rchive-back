package kr.sparta.rchive.domain.post.dto.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record TagCreateReq(
        @Schema(description = "태그 이름", example = "[\"string1\", \"string2\"]")
        List<String> tagNameList
) {

}
