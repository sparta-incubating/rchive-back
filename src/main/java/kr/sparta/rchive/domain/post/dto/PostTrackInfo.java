package kr.sparta.rchive.domain.post.dto;

import kr.sparta.rchive.domain.post.entity.Post;
import kr.sparta.rchive.domain.user.entity.Track;
import lombok.Builder;

@Builder
public record PostTrackInfo(
    Post post,
    Track track
) {

}
