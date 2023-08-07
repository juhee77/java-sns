package com.lahee.mutsasns.controller;

import com.lahee.mutsasns.domain.FriendshipStatus;
import com.lahee.mutsasns.dto.ApiResponse;
import com.lahee.mutsasns.dto.MessageResponse;
import com.lahee.mutsasns.dto.friendShip.FriendShipResponseDto;
import com.lahee.mutsasns.service.FriendShipService;
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
public class FriendShipController {
    private final FriendShipService friendShipService;

    @Operation(summary = "친구 신청")
    @PostMapping("/friend-ship/{friendName}")
    public ApiResponse<MessageResponse> suggestToFriend(
            @PathVariable("username") String username,
            @PathVariable("friendName") String friendName
    ){
        friendShipService.suggestToFriend(username,friendName, getCurrentUsername());
        return ApiResponse.success(MessageResponse.getInstance("친구신청을 보냈슴다"));
    }

    @Operation(summary = "나에게 온 친구 신청 목록")
    @GetMapping("/friend-ship")
    public ApiResponse<List<FriendShipResponseDto>> suggestFromFriend(
            @PathVariable("username") String username
    ){
        return ApiResponse.success(friendShipService.suggestFromFriend(username,getCurrentUsername()));
    }

    @Operation(summary = "나에게 온 친구 신청 수락,거절 여부")
    @PutMapping("/friend-ship/{friendShipId}")
    public ApiResponse<FriendShipResponseDto> follow(
            @PathVariable("username") String username,
            @PathVariable("friendShipId") Long friendShipId,
            @RequestParam("status")FriendshipStatus status
    ){
        return ApiResponse.success(friendShipService.updateStatus(username,status, friendShipId,getCurrentUsername()));
    }

}
