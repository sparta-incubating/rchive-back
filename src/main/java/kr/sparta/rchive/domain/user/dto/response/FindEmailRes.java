package kr.sparta.rchive.domain.user.dto.response;

import jakarta.validation.constraints.Email;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record FindEmailRes(
        String email,
        LocalDate createdAt
) {

}
