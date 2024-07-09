package kr.sparta.rchive.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;

@Builder
@JsonIgnoreProperties
public record RoleGetTrackNameListReq (
        List<String> trackNameList
    ) {

}
