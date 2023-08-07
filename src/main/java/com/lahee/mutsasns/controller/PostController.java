package com.lahee.mutsasns.controller;

import com.lahee.mutsasns.dto.ApiResponse;
import com.lahee.mutsasns.dto.post.PostRequestDto;
import com.lahee.mutsasns.dto.post.PostResponseDto;
import com.lahee.mutsasns.service.PostService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.lahee.mutsasns.util.SecurityUtil.getCurrentUsername;

@RequestMapping("api/post")
@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private final PostService postService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ApiResponse<PostResponseDto> savePostWithImage(
            @Valid @RequestPart(value = "postRequestDto") @Parameter(schema = @Schema(type = "string", format = ("binary"))) PostRequestDto postRequestDto,
//            @Valid @RequestPart @Parameter(name = "postRequestDto",content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE )) PostRequestDto postRequestDto,
            @RequestPart(name = "files", required = false) @Parameter(required = false, description = "Files to be uploaded", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) List<MultipartFile> files) {

//            @RequestPart(value = "files", required = false) List<MultipartFile> files) {
        log.info("ERRORORORORO {}", postRequestDto.toString());
        PostResponseDto postResponseDto = postService.saveWithImages(postRequestDto, files, getCurrentUsername());
        return ApiResponse.success(postResponseDto);
    }


}
