package kr.sparta.rchive.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import kr.sparta.rchive.domain.user.enums.GenderEnum;
import kr.sparta.rchive.domain.user.enums.OAuthTypeEnum;
import kr.sparta.rchive.domain.user.enums.UserRoleEnum;
import lombok.Builder;

@Builder
@JsonIgnoreProperties
public record UserSignupReq(
        //String oAuthId,
        OAuthTypeEnum oAuthType,
        @Email(message = "올바르지 않은 이메일 형식")
        String email,
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{6,20}$", message = "비밀번호는 6~20자 영문자와 숫자를 포함해야 합니다")
        String password,
        String username,
        LocalDate birth,
        @Pattern(regexp = "^[0-9]{11}$", message = "휴대폰번호는 숫자만 11자리")
        String phone,
        GenderEnum gender,
        String profileImg,
        @Pattern(regexp = "^[가-힣a-zA-Z0-9]{2,10}$|^$", message = "닉네임은 특수문자를 제외한 2~10자리 또는 빈 값이어야 합니다.")
        String nickname,
        UserRoleEnum userRole,
        Boolean termUserAge,
        Boolean termUseService,
        Boolean termPersonalInfo,
        Boolean termAdvertisement

) {

}
