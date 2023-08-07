package com.lahee.mutsasns.controller;

import com.lahee.mutsasns.constants.constants;
import com.lahee.mutsasns.dto.ApiResponse;
import com.lahee.mutsasns.dto.MessageResponse;
import com.lahee.mutsasns.dto.following.FollowingResponseDto;
import com.lahee.mutsasns.service.FollowingService;
import com.lahee.mutsasns.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.lahee.mutsasns.util.SecurityUtil.getCurrentUsername;

@RequestMapping("api/users/{username}")
@RestController
@RequiredArgsConstructor
@Slf4j
public class FollowingController {
    private final FollowingService followingService;
    private final UserService userService;

    @Operation(summary = "팔로우")
    @PostMapping("/follow/{followerName}")
    public ApiResponse<FollowingResponseDto> follow(
            @PathVariable("username") String username,
            @PathVariable("followerName") String followingName
    ){
        return ApiResponse.success(followingService.follow(username,followingName, getCurrentUsername()));
    }

    @Operation(summary = "팔로우 삭제")
    @DeleteMapping("/follow/{followerName}")
    public ApiResponse<MessageResponse> unfollow(
            @PathVariable("username") String username,
            @PathVariable("followerName") String followingName
    ) {
        followingService.unfollow(username,followingName, getCurrentUsername());
        return ApiResponse.success(MessageResponse.getInstance(constants.UNFOLLOW_MSG));
    }

    @Operation(summary = "내가 팔로우한 사람들 목록")
    @GetMapping("/following")
    public ApiResponse<List<FollowingResponseDto>> getFollowingList(
            @PathVariable("username") String username
    ){
        return ApiResponse.success(userService.getFollowingList(username,getCurrentUsername()));
    }

    @Operation(summary = "나를 팔로우한 사람들 목록")
    @GetMapping("/follower")
    public ApiResponse<List<FollowingResponseDto>> getFollowerList(
            @PathVariable("username") String username
    ){
        return ApiResponse.success(userService.getFollowerList(username,getCurrentUsername()));
    }


}
