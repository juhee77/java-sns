package com.lahee.mutsasns.service;

import com.lahee.mutsasns.domain.Following;
import com.lahee.mutsasns.domain.User;
import com.lahee.mutsasns.dto.following.FollowingResponseDto;
import com.lahee.mutsasns.exception.CustomException;
import com.lahee.mutsasns.exception.ErrorCode;
import com.lahee.mutsasns.repository.FollowingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowingService {
    private final UserService userService;
    private final FollowingRepository followingRepository;

    @Transactional
    public FollowingResponseDto follow(String username, String followingName, String currentUsername) {
        if (!username.equals(currentUsername)) { //현재 경로 유효성을 검증한다.
            throw new CustomException(ErrorCode.ERROR_UNAUTHORIZED);
        }
        User user = userService.getUser(username);
        User following = userService.getUser(followingName);

        Following follow = Following.getInstance(user, following);
        return FollowingResponseDto.fromEntity(followingRepository.save(follow));
    }

    @Transactional
    public void unfollow(String username, String followingName, String currentUsername) {
        if (!username.equals(currentUsername)) { //현재 경로 유효성을 검증한다.
            throw new CustomException(ErrorCode.ERROR_UNAUTHORIZED);
        }

        User user = userService.getUser(username);
        User following = userService.getUser(followingName);

        if (!followingRepository.existsByFollowerAndFollowing(user, following)) {
            throw new CustomException(ErrorCode.ERROR_NOT_FOUND);//팔로우 되어있지 않은 경우
        }

        followingRepository.deleteByFollowerAndFollowing(user, following);
    }
}
