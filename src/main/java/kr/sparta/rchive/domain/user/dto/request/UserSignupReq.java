package kr.sparta.rchive.domain.user.dto.request;

import java.time.LocalDate;
import kr.sparta.rchive.domain.user.enums.GenderEnum;
import kr.sparta.rchive.domain.user.enums.OAuthTypeEnum;
import kr.sparta.rchive.domain.user.enums.UserRoleEnum;
import lombok.Builder;

@Builder
public record UserSignupReq (
        String oAuthId,
        OAuthTypeEnum oAuthType,
        String email,
        String password,
        LocalDate birth,
        String phone,
        GenderEnum gender,
        String nickname,
        UserRoleEnum userRole
        ) {

}
