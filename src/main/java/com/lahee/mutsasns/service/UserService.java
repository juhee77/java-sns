package com.lahee.mutsasns.service;


import com.lahee.mutsasns.domain.*;
import com.lahee.mutsasns.dto.following.FollowingResponseDto;
import com.lahee.mutsasns.dto.post.PostResponseDto;
import com.lahee.mutsasns.dto.user.LoginDto;
import com.lahee.mutsasns.dto.user.SignupDto;
import com.lahee.mutsasns.dto.user.TokenDto;
import com.lahee.mutsasns.dto.user.UserResponseDto;
import com.lahee.mutsasns.exception.CustomException;
import com.lahee.mutsasns.exception.ErrorCode;
import com.lahee.mutsasns.jwt.JwtTokenUtils;
import com.lahee.mutsasns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {
    private final JwtTokenUtils jwtTokenUtils;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final FileService fileService;

    public TokenDto login(LoginDto loginDto) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getUsername());
        if (!passwordEncoder.matches(loginDto.getPassword(), userDetails.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
        TokenDto tokenDto = jwtTokenUtils.generateToken(userDetails);
        return tokenDto;
    }

    @Transactional
    public UserResponseDto signup(SignupDto signupDto) {
        if (userRepository.findByUsername(signupDto.getUsername()).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_USED_USERNAME);
        }

        User user = new User().builder()
                .username(signupDto.getUsername())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .email(signupDto.getEmail())
                .phone(signupDto.getPhoneNumber())
                .build();

        return UserResponseDto.fromEntity(userRepository.save(user));
    }

    @Transactional
    public UserResponseDto saveUserImage(String username, MultipartFile image, String currentUsername) {
        //user찾기
        if (!username.equals(currentUsername)) {
            throw new CustomException(ErrorCode.ERROR_BAD_REQUEST); //유저 이름과 현재 로그인한 유저가 다르다, 잘못된 URL경로
        }
        User user = getUser(currentUsername);
        if (user.getImage() != null) {
            fileService.dropFile(user.getImage());
        }
        File file = fileService.saveOneFile(FolderType.USER, user.getId(), image);
        user.updateProfileImage(file);
        return UserResponseDto.fromEntity(user);
    }

    public UserResponseDto getUserDto(String username) {
        return UserResponseDto.fromEntity(getUser(username));
    }

    public User getUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        return user.get();
    }

    public List<PostResponseDto> getPost(String username) {
        User user = getUser(username);
        List<PostResponseDto> postResponseDtos = new ArrayList<>();
        for (Post post : user.getPosts()) {
            postResponseDtos.add(PostResponseDto.fromEntity(post));
        }
        return postResponseDtos;
    }

    public List<FollowingResponseDto> getFollowingList(String name, String currentName) {
        if (!name.equals(currentName))
            throw new CustomException(ErrorCode.ERROR_UNAUTHORIZED);

        User user = getUser(name);
        List<FollowingResponseDto> dtos = new ArrayList<>();
        for (Following following : user.getFollowing()) {
            dtos.add(FollowingResponseDto.fromEntity(following));
        }
        return dtos;
    }

    public List<FollowingResponseDto> getFollowerList(String name, String currentName) {
        if (!name.equals(currentName))
            throw new CustomException(ErrorCode.ERROR_UNAUTHORIZED);

        User user = getUser(name);
        List<FollowingResponseDto> dtos = new ArrayList<>();
        for (Following follower : user.getFollower()) {
            dtos.add(FollowingResponseDto.fromEntity(follower));
        }
        return dtos;
    }

    public List<PostResponseDto> getFollowingPost(String name, String currentName) {
        if (!name.equals(currentName))
            throw new CustomException(ErrorCode.ERROR_UNAUTHORIZED);

        User user = getUser(name);
        List<Following> following = user.getFollowing();

        List<Post> posts = following.stream()
                .flatMap(followingUser -> followingUser.getFollowing().getPosts().stream())
                .sorted(Comparator.comparingLong(Post::getId).reversed())
                .collect(Collectors.toList());

        return posts.stream()
                .map(PostResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

}
