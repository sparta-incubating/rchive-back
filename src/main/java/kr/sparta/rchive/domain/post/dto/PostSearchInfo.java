package kr.sparta.rchive.domain.post.dto;

import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.post.entity.Tag;
import lombok.Builder;

import java.util.List;

@Builder
public record PostSearchInfo(
        Post post,
        List<Tag> tagNameList
) {
}
