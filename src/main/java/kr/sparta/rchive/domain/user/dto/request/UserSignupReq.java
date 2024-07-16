package kr.sparta.rchive.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import kr.sparta.rchive.domain.user.enums.GenderEnum;
import kr.sparta.rchive.domain.user.enums.OAuthTypeEnum;
import kr.sparta.rchive.domain.user.enums.UserRoleEnum;
import lombok.Builder;

@Builder
@JsonIgnoreProperties
public record UserSignupReq (
        //String oAuthId,
        OAuthTypeEnum oAuthType,
        String email,
        String password,
        String username,
        LocalDate birth,
        String phone,
        GenderEnum gender,
        String profileImg,
        String nickname,
        UserRoleEnum userRole,
        Boolean termUserAge,
        Boolean termUseService,
        Boolean termPersonalInfo,
        Boolean termAdvertisement

        ) {

}
