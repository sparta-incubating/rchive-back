package kr.sparta.rchive.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;

@Builder
@JsonIgnoreProperties
public record UserLoginReq(
        String username,
        String password
        ) {

}
