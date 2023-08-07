package com.lahee.mutsasns.controller;

import com.lahee.mutsasns.dto.ApiResponse;
import com.lahee.mutsasns.dto.MessageResponse;
import com.lahee.mutsasns.dto.comment.CommentRequestDto;
import com.lahee.mutsasns.dto.comment.CommentResponseDto;
import com.lahee.mutsasns.service.CommentService;
import com.lahee.mutsasns.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.lahee.mutsasns.constants.constants.DELETED_COMMENT_MSG;

@RequestMapping("api/post/{postId}/comment")
@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ApiResponse<CommentResponseDto> saveComment(
            @PathVariable("postId") Long postId,
            @Valid @RequestBody CommentRequestDto commentRequestDto
    ) {
        return ApiResponse.success(commentService.saveComment(postId, commentRequestDto, SecurityUtil.getCurrentUsername()));
    }

    @PutMapping("/{commentId}")
    public ApiResponse<CommentResponseDto> updateCommend(
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId,
            @Valid @RequestBody CommentRequestDto commentRequestDto
    ) {
        return ApiResponse.success(commentService.updateComment(postId, commentId,commentRequestDto, SecurityUtil.getCurrentUsername()));
    }

    @DeleteMapping("/{commentId}")
    public ApiResponse<MessageResponse> updateCommend(
            @PathVariable("postId") Long postId,
            @PathVariable("commentId") Long commentId
    ) {
        commentService.deleteComment(postId, commentId, SecurityUtil.getCurrentUsername());
        return ApiResponse.success(MessageResponse.getInstance(DELETED_COMMENT_MSG));
    }
}
