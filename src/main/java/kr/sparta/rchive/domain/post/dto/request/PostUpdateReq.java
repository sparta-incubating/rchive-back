package kr.sparta.rchive.domain.post.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.sparta.rchive.domain.post.enums.PostTypeEnum;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import lombok.Builder;

@Builder
@JsonIgnoreProperties
public record PostUpdateReq(
        @Schema(description = "카테고리", example = "Sparta_Lecture")
        PostTypeEnum postType,
        @Schema(description = "제목", example = "게시물 제목")
        String title,
        @Schema(description = "튜터 ID", example = "1")
        Long tutorId,
        @Schema(description = "업로드 날짜", example = "1999-01-01")
        LocalDate uploadedAt,
        @Schema(description = "썸네일 URL", example = "www.www.com")
        String thumbnailUrl,
        @Schema(description = "영상 링크", example = "www.youtube.com")
        String videoLink,
        @Schema(description = "게시물 링크", example = "www.notion.so")
        String contentLink,
        @Schema(description = "내용", example = "게시물 내용")
        String content,
        @Schema(description = "태그 이름", example = "[\"string1\", \"string2\"]")
        List<String> tagNameList,
        @Schema(description = "트랙 기수", example = "1")
        Integer updatePeriod,
        @Schema(description = "열람 가능 여부", example = "true")
        Boolean isOpened
) {

}
