package kr.sparta.rchive.domain.user.dto.request;

import lombok.Builder;

@Builder
public record ProfileUpdateProfileImgReq(
        String profileImg
) {

}
