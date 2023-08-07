package com.lahee.mutsasns.service;

import com.lahee.mutsasns.domain.File;
import com.lahee.mutsasns.domain.FolderType;
import com.lahee.mutsasns.domain.Post;
import com.lahee.mutsasns.domain.User;
import com.lahee.mutsasns.dto.post.PostDetailsResponseDto;
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
        post.validSameUser(user);//해당 유저의 포스트가 맞는지 확인한다.

        dropPostFiles(post);

        post.uploadFiles(fileService.saveMultiFile(FolderType.POST, post.getId(), files));
        return PostResponseDto.fromEntity(post);
    }

    @Transactional
    public PostResponseDto savePostThumbnail(Long id, MultipartFile file, String currentUsername) {
        User user = userService.getUser(currentUsername);
        Post post = getPost(id);
        post.validSameUser(user);//해당 유저의 포스트가 맞는지 확인한다.

        dropThumbnailImage(post);

        post.uploadThumbnail(fileService.saveOneFile(FolderType.POST_THUMB, post.getId(), file));
        return PostResponseDto.fromEntity(post);
    }

    @Transactional
    public PostResponseDto saveWithImages(PostRequestDto postRequestDto, List<MultipartFile> files, MultipartFile file, String currentUsername) {
        PostResponseDto save = save(postRequestDto, currentUsername);
        if (file != null) save = savePostThumbnail(save.getId(), file, currentUsername);
        else if (file == null && files != null) {
            save = savePostThumbnail(save.getId(), files.get(0), currentUsername);
        }
        if (files != null) save = savePostImages(save.getId(), files, currentUsername);

        return save;
    }

    public PostDetailsResponseDto getPostById(Long postId) {
        Post post = getPost(postId);
        return PostDetailsResponseDto.fromEntity(post);
    }

    @Transactional
    public PostResponseDto updatePostById(Long postId, PostRequestDto postRequestDto, List<MultipartFile> files, MultipartFile file, String currentUsername) {
        User user = userService.getUser(currentUsername);
        Post post = getPost(postId);
        post.validSameUser(user);//수정 권한이 있는 사람인지

        //내용 업데이트
        PostResponseDto save = updatePost(post, postRequestDto);

        //이미지 업데이트
        if (file != null) save = savePostThumbnail(postId, file, currentUsername);
        else if (file == null && files != null) {
            save = savePostThumbnail(postId, files.get(0), currentUsername);
        }
        if (files != null) save = savePostImages(postId, files, currentUsername);

        return save;
    }

    @Transactional
    public PostResponseDto updatePost(Post post, PostRequestDto postRequestDto) {
        post.update(postRequestDto);
        return PostResponseDto.fromEntity(post);

    }

    @Transactional
    public void deletePostById(Long postId, String username) {
        User user = userService.getUser(username);
        Post post = getPost(postId);
        post.validSameUser(user);

        //이미지 드랍
        dropPostFiles(post);
        dropThumbnailImage(post);
        postRepository.delete(post);
    }

    private void dropPostFiles(Post post) {
        if (post.getPostfiles() != null || post.getPostfiles().size() > 0) { //기존에 이미지가 있는 경우 드랍
            for (File postfile : post.getPostfiles()) {
                fileService.dropFile(postfile);
            }
        }
    }

    private void dropThumbnailImage(Post post) {
        if (post.getThumbnail() != null) {
            fileService.dropFile(post.getThumbnail());
        }
    }

    public Post getPost(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            throw new CustomException(ErrorCode.POST_NOT_FOUND);
        }
        return post.get();
    }
}
