package kr.sparta.rchive.domain.post.dto.request;

import java.time.LocalDate;
import java.util.List;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import kr.sparta.rchive.domain.user.enums.TrackEnum;
import lombok.Builder;

@Builder
public record PostCreateReq(
        PostTypeEnum postType,
        String title,
        String tutor,
        LocalDate uploadedAt,
        String video,
        String content,
        List<String> tagNames,
        TrackEnum track,
        int period
) {

}
