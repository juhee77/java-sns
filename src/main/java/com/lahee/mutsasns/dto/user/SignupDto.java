package com.lahee.mutsasns.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupDto {
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
}
