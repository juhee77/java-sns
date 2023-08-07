package com.lahee.mutsasns.service;

import com.lahee.mutsasns.domain.Comment;
import com.lahee.mutsasns.domain.Post;
import com.lahee.mutsasns.domain.User;
import com.lahee.mutsasns.dto.comment.CommentRequestDto;
import com.lahee.mutsasns.dto.comment.CommentResponseDto;
import com.lahee.mutsasns.exception.CustomException;
import com.lahee.mutsasns.exception.ErrorCode;
import com.lahee.mutsasns.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CommentService {
    private final UserService userService;
    private final PostService postService;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponseDto saveComment(Long postId, CommentRequestDto commentRequestDto, String currentUsername) {
        User user = userService.getUser(currentUsername);
        Post post = postService.getPost(postId);

        Comment entityInstance = Comment.getEntityInstance(commentRequestDto, user, post);
        return CommentResponseDto.fromEntity(commentRepository.save(entityInstance));
    }

    @Transactional
    public CommentResponseDto updateComment(Long postId, Long commentId, CommentRequestDto commentRequestDto, String currentUsername) {
        User user = userService.getUser(currentUsername);
        Post post = postService.getPost(postId);
        Comment comment = getComment(commentId);

        comment.validPost(post);
        comment.validUser(user);

        comment.update(commentRequestDto);
        return CommentResponseDto.fromEntity(comment);
    }

    @Transactional
    public void deleteComment(Long postId, Long commentId, String currentUsername) {
        User user = userService.getUser(currentUsername);
        Post post = postService.getPost(postId);
        Comment comment = getComment(commentId);

        comment.validPost(post);
        comment.validUser(user);

        commentRepository.deleteById(commentId);
    }

    public Comment getComment(Long commendId) {
        Optional<Comment> comment = commentRepository.findById(commendId);
        if (comment.isEmpty()) {
            throw new CustomException(ErrorCode.COMMENT_NOT_FOUND);
        }
        return comment.get();
    }

}
