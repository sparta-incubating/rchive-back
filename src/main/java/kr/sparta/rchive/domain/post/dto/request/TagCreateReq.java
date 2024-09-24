package kr.sparta.rchive.domain.post.dto.request;

import java.util.List;
import lombok.Builder;

@Builder
public record TagCreateReq(
        List<String> tagNameList
) {

}
