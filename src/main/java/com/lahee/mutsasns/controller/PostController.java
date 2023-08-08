package com.lahee.mutsasns.controller;

import com.lahee.mutsasns.dto.ApiResponse;
import com.lahee.mutsasns.dto.MessageResponse;
import com.lahee.mutsasns.dto.post.PostDetailsResponseDto;
import com.lahee.mutsasns.dto.post.PostRequestDto;
import com.lahee.mutsasns.dto.post.PostResponseDto;
import com.lahee.mutsasns.service.PostService;
import com.lahee.mutsasns.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.lahee.mutsasns.constants.constants.DELETE_POST_MSG;
import static com.lahee.mutsasns.util.SecurityUtil.getCurrentUsername;

@RequestMapping("api/post")
@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;

    @Operation(summary = "피드 작성")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ApiResponse<PostResponseDto> savePostWithImage(
            @Valid @RequestPart(value = "postRequestDto") @Parameter(schema = @Schema(type = "string", format = ("binary"))) PostRequestDto postRequestDto,
            @RequestPart(name = "files", required = false) @Parameter(description = "포스트 이미지") List<MultipartFile> files,
            @RequestPart(name = "file", required = false) @Parameter(description = "포스트 썸네일 이미지 등록하지 않는 경우 자동으로 처음이미지 설정") MultipartFile file
    ) {
        PostResponseDto postResponseDto = postService.saveWithImages(postRequestDto, files, file, getCurrentUsername());
        return ApiResponse.success(postResponseDto);
    }

    @Operation(summary = "피드 수정")
    @PutMapping(value = "/{postId}",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ApiResponse<PostResponseDto> updatePost(
            @PathVariable("postId") Long postId,
            @Valid @RequestPart(value = "postRequestDto") @Parameter(schema = @Schema(type = "string", format = ("binary"))) PostRequestDto postRequestDto,
            @RequestPart(name = "files", required = false) @Parameter(description = "포스트 이미지") List<MultipartFile> files,
            @RequestPart(name = "file", required = false) @Parameter(description = "포스트 썸네일 이미지 등록하지 않는 경우 자동으로 처음이미지 설정") MultipartFile file
    ) {
        PostResponseDto postById = postService.updatePostById(postId, postRequestDto, files, file, SecurityUtil.getCurrentUsername());
        return ApiResponse.success(postById);
    }

    @Operation(summary = "피드 단일 조회")
    @GetMapping("/{postId}")
    public ApiResponse<PostDetailsResponseDto> getOnePost(@PathVariable("postId") Long postId
    ) {
        PostDetailsResponseDto postById = postService.getPostById(postId);
        return ApiResponse.success(postById);
    }

    @Operation(summary = "피드 삭제")
    @DeleteMapping("/{postId}")
    public ApiResponse<MessageResponse> deletePost(
            @PathVariable("postId") Long postId
    ) {
        postService.deletePostById(postId,getCurrentUsername());
        return ApiResponse.success(MessageResponse.getInstance(DELETE_POST_MSG));
    }
}
