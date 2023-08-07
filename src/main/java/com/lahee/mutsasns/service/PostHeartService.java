package com.lahee.mutsasns.service;

import com.lahee.mutsasns.domain.Post;
import com.lahee.mutsasns.domain.PostHeart;
import com.lahee.mutsasns.domain.User;
import com.lahee.mutsasns.repository.PostHeartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PostHeartService {
    private final UserService userService;
    private final PostService postService;
    private final PostHeartRepository postHeartRepository;

    public boolean isAlreadyLike(Long postId, String currentUsername) {
        User user = userService.getUser(currentUsername);
        Post post = postService.getPost(postId);

        return postHeartRepository.existsByUserAndPost(user, post);
    }

    @Transactional
    public void deleteLike(Long postId, String currentUsername) {
        User user = userService.getUser(currentUsername);
        Post post = postService.getPost(postId);

        postHeartRepository.deleteByUserAndPost(user, post);
    }

    @Transactional
    public void saveLike(Long postId, String currentUsername) {
        User user = userService.getUser(currentUsername);
        Post post = postService.getPost(postId);

        //자신의 게시글에는 하트를 누를 수 없다.
        post.validNotSameUser(user);

        PostHeart postHeart = PostHeart.getInstance(post, user);

        postHeartRepository.save(postHeart);
    }
}
