package com.lahee.mutsasns.controller;

import com.lahee.mutsasns.dto.ApiResponse;
import com.lahee.mutsasns.dto.user.LoginDto;
import com.lahee.mutsasns.dto.user.SignupDto;
import com.lahee.mutsasns.dto.user.TokenDto;
import com.lahee.mutsasns.dto.user.UserResponseDto;
import com.lahee.mutsasns.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/user/auth")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserAuthController {
    private final UserService userService;

    @Operation(summary = "로그인")
    @PostMapping("login")
    public ApiResponse<TokenDto> login(@Valid @RequestBody LoginDto dto) {
        TokenDto tokenDto = userService.login(dto);
        return ApiResponse.success(tokenDto);
    }

    @Operation(summary = "회원가입")
    @PostMapping("signup")
    public ApiResponse<UserResponseDto> signup(@Valid @RequestBody SignupDto signupDto) {
        return ApiResponse.success(userService.signup(signupDto));
    }
}
