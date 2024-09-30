package kr.sparta.rchive.domain.post.dto.request;

public record PostUpdateContentReq(
    String contentLink,
    String content
) {

}
