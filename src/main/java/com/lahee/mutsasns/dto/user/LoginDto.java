package com.lahee.mutsasns.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @NotBlank
    @Schema(defaultValue = "username")
    private String username;
    @NotBlank
    @Schema(defaultValue = "password")
    private String password;
}
