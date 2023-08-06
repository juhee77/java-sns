package com.lahee.mutsasns.controller;

import com.lahee.mutsasns.dto.ApiResponse;
import com.lahee.mutsasns.dto.user.LoginDto;
import com.lahee.mutsasns.dto.user.SignupDto;
import com.lahee.mutsasns.dto.user.TokenDto;
import com.lahee.mutsasns.dto.user.UserResponseDto;
import com.lahee.mutsasns.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("user/auth")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserAuthController {
    private final UserService userService;

    @PostMapping("login")
    public ApiResponse<TokenDto> login(@Valid @RequestBody LoginDto dto) {
        TokenDto tokenDto = userService.login(dto);
        return ApiResponse.success(tokenDto);
    }

    @PostMapping("signup")
    public ApiResponse<UserResponseDto> signup(@Valid @RequestBody SignupDto signupDto) {
        return ApiResponse.success(userService.signup(signupDto));
    }
}
