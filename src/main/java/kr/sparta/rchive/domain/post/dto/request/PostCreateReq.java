package kr.sparta.rchive.domain.post.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.util.List;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import lombok.Builder;

@Builder
@JsonIgnoreProperties
public record PostCreateReq(
        PostTypeEnum postType,
        String title,
        String tutor,
        LocalDate uploadedAt,
        String thumbnailUrl,
        String videoLink,
        String contentLink,
        String content,
        List<String> tagNameList,
        TrackNameEnum trackName,
        Integer period,
        Boolean isOpened
) {

}