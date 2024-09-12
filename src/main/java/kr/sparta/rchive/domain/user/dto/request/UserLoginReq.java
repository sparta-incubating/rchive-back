package kr.sparta.rchive.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@JsonIgnoreProperties
public record UserLoginReq(
        @Schema(description = "이메일", example = "email@email.com")
        String username,
        
        @Schema(description = "비밀번호", example = "user123")
        String password
) {

}
