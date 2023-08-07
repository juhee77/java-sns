package com.lahee.mutsasns.service;

import com.lahee.mutsasns.domain.File;
import com.lahee.mutsasns.domain.FolderType;
import com.lahee.mutsasns.domain.Post;
import com.lahee.mutsasns.domain.User;
import com.lahee.mutsasns.dto.post.PostRequestDto;
import com.lahee.mutsasns.dto.post.PostResponseDto;
import com.lahee.mutsasns.exception.CustomException;
import com.lahee.mutsasns.exception.ErrorCode;
import com.lahee.mutsasns.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PostService {
    private final UserService userService;
    private final PostRepository postRepository;
    private final FileService fileService;

    @Transactional
    public PostResponseDto save(PostRequestDto postRequestDto, String username) {
        User user = userService.getUser(username);

        Post post = Post.getEntityInstance(postRequestDto, user);
        return PostResponseDto.fromEntity(postRepository.save(post));
    }

    @Transactional
    public PostResponseDto savePostImages(Long id, List<MultipartFile> files, String currentUsername) {
        User user = userService.getUser(currentUsername);
        Post post = getPost(id);
        post.validUser(user);//해당 유저의 포스트가 맞는지 확인한다.

        if (post.getPostfiles() != null || post.getPostfiles().size() > 0) {
            for (File postfile : post.getPostfiles()) {
                fileService.dropFile(postfile);
            }
        }

        post.uploadFiles(fileService.saveMultiFile(FolderType.POST, post.getId(), files));
        return PostResponseDto.fromEntity(post);
    }

    @Transactional
    public PostResponseDto saveWithImages(PostRequestDto postRequestDto, List<MultipartFile> files, String currentUsername) {
        PostResponseDto save = save(postRequestDto, currentUsername);
        if (files != null) save = savePostImages(save.getId(), files, currentUsername);
        return save;
    }

    public Post getPost(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND_EXCEPTION);
        }
        return post.get();
    }
}