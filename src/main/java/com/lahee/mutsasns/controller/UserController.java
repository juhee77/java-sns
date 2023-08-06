package com.lahee.mutsasns.controller;


import com.lahee.mutsasns.dto.ApiResponse;
import com.lahee.mutsasns.dto.user.UserResponseDto;
import com.lahee.mutsasns.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.lahee.mutsasns.util.SecurityUtil.getCurrentUsername;

@RequestMapping("api/user")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/my/profile")
    @Operation(summary = "현재 유저 확인")
    public ApiResponse<UserResponseDto> login() {
        return new ApiResponse<>(HttpStatus.OK, userService.getUserDto(getCurrentUsername()));
    }


    @RequestMapping(value = "/{username}/image", method = {RequestMethod.PUT, RequestMethod.POST}, consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "유저 사진 업로드")
    public ApiResponse<UserResponseDto> saveItemImage(@PathVariable("username") String username, @RequestPart("image") MultipartFile image) {
        UserResponseDto userResponseDto = userService.saveUserImage(username, image, getCurrentUsername());
        return ApiResponse.success(userResponseDto);
    }
}
