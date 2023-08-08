package com.lahee.mutsasns.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupDto {
    @NotBlank
    @Schema(defaultValue = "username")
    private String username;
    @NotBlank
    @Schema(defaultValue = "password")
    private String password;
    @Email(message = "이메일 형식으로 입력해주세요") @NotBlank
    @Schema(defaultValue = "string@email.com")
    private String email;
    @NotBlank
    @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "전화 번호를 확인해주세요")
    @Schema(defaultValue = "010-1234-5678")
    private String phoneNumber;
}
