package com.lahee.mutsasns.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

}
