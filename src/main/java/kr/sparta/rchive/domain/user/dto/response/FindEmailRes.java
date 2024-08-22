package kr.sparta.rchive.domain.user.dto.response;

import jakarta.validation.constraints.Email;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record FindEmailRes(
        @Email(message = "올바르지 않은 이메일 형식")
        String email,
        LocalDate createdAt
) {

}
