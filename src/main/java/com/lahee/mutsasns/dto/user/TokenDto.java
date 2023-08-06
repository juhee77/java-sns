package com.lahee.mutsasns.dto.user;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TokenDto { //token
    private String grantType;
    private String accessToken;
    private Long accessTokenExpiresIn;
}
