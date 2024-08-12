package kr.sparta.rchive.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.time.LocalDateTime;
import kr.sparta.rchive.domain.user.enums.TrackNameEnum;
import kr.sparta.rchive.domain.user.enums.TrackRoleEnum;
import lombok.Builder;

@Builder
public record UserRes (
        String email,
        String username,
        String profileImg,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String nickname,
        LocalDate birth,
        String phone,
        TrackRoleEnum trackRole,
        TrackNameEnum trackName,
        int period

){

}