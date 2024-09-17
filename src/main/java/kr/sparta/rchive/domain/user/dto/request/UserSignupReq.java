package kr.sparta.rchive.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
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
        @Schema(description = "oAuthType", example = "LOCAL")
        OAuthTypeEnum oAuthType,

        @Schema(description = "이메일", example = "email@email.com")
        @Email(message = "올바르지 않은 이메일 형식")
        String email,

        @Schema(description = "비밀번호", example = "user123")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{6,20}$", message = "비밀번호는 6~20자 영문자와 숫자를 포함해야 합니다")
        String password,

        @Schema(description = "이름", example = "아무개")
        String username,

        @Schema(description = "생년월일", example = "2000-01-01")
        LocalDate birth,

        @Schema(description = "휴대폰 번호", example = "01012345678")
        @Pattern(regexp = "^[0-9]{11}$", message = "휴대폰번호는 숫자만 11자리")
        String phone,

        @Schema(description = "성별", example = "NONE")
        GenderEnum gender,

        @Schema(description = "프로필 이미지", example = "default")
        String profileImg,

        @Schema(description = "유저 권한", example = "USER")
        UserRoleEnum userRole,

        @Schema(description = "만 14세 이상 동의", example = "true")
        Boolean termUserAge,

        @Schema(description = "서비스 약관 동의", example = "true")
        Boolean termUseService,

        @Schema(description = "개인정보처리방침 및 제3자 제공 동의", example = "true")
        Boolean termPersonalInfo,

        @Schema(description = "광고성 정보 수신 동의", example = "true")
        Boolean termAdvertisement

) {

}
