package kr.sparta.rchive.domain.post.dto.request;

import kr.sparta.rchive.domain.user.enums.TrackNameEnum;

public record DeleteThumbnailReq(
    TrackNameEnum trackName,
    Integer period
) {

}
