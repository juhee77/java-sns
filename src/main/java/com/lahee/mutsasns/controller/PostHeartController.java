package com.lahee.mutsasns.controller;

import com.lahee.mutsasns.dto.ApiResponse;
import com.lahee.mutsasns.dto.MessageResponse;
import com.lahee.mutsasns.service.PostHeartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.lahee.mutsasns.constants.constants.LIKE_DOWN;
import static com.lahee.mutsasns.constants.constants.LIKE_UP;
import static com.lahee.mutsasns.util.SecurityUtil.getCurrentUsername;

@RequestMapping("api/like/{postId}")
@RestController
@RequiredArgsConstructor
@Slf4j
public class PostHeartController {
    private final PostHeartService postHeartService;

    @PostMapping
    public ApiResponse<MessageResponse> like(
            @PathVariable("postId") Long postId
    ) {
        String currentUsername = getCurrentUsername();
        if (postHeartService.isAlreadyLike(postId, currentUsername)) {
            //좋아요 취소
            postHeartService.deleteLike(postId, getCurrentUsername());
            return ApiResponse.success(MessageResponse.getInstance(LIKE_DOWN));
        } else {
            //좋아요
            postHeartService.saveLike(postId, getCurrentUsername());
            return ApiResponse.success(MessageResponse.getInstance(LIKE_UP));
        }
    }
}
