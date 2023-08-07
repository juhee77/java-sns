package com.lahee.mutsasns.service;

import com.lahee.mutsasns.domain.FriendShip;
import com.lahee.mutsasns.domain.FriendshipStatus;
import com.lahee.mutsasns.domain.Post;
import com.lahee.mutsasns.domain.User;
import com.lahee.mutsasns.dto.friendShip.FriendShipResponseDto;
import com.lahee.mutsasns.dto.post.PostResponseDto;
import com.lahee.mutsasns.exception.CustomException;
import com.lahee.mutsasns.exception.ErrorCode;
import com.lahee.mutsasns.repository.FriendShipRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendShipService {
    private final FriendShipRepository friendShipRepository;
    private final UserService userService;

    @Transactional
    public FriendShipResponseDto suggestToFriend(String username, String friendName, String currentUsername) {
        if (!username.equals(currentUsername)) { //현재 경로 유효성을 검증한다.
            throw new CustomException(ErrorCode.ERROR_UNAUTHORIZED);
        }
        User sender = userService.getUser(username);
        User receiver = userService.getUser(friendName);

        //보류중인 같은 제안이 있는지 확인한다.(내가 친구에게 한 경우와, 친구가 나에게 한경우)
        if (friendShipRepository.existsBySenderAndReceiverAndStatus(sender, receiver, FriendshipStatus.PENDING) ||
                friendShipRepository.existsByReceiverAndSenderAndStatus(receiver, sender, FriendshipStatus.PENDING)) {
            throw new CustomException(ErrorCode.DUPLICATE_SUGGEST);
        }

        FriendShip friendShip = FriendShip.getInstance(sender, receiver);
        return FriendShipResponseDto.fromEntity(friendShipRepository.save(friendShip));
    }


    public List<FriendShipResponseDto> suggestFromFriend(String username, String currentUsername) {
        if (!username.equals(currentUsername)) { //현재 경로 유효성을 검증한다.
            throw new CustomException(ErrorCode.ERROR_UNAUTHORIZED);
        }
        User receiver = userService.getUser(username);
        return friendShipRepository.findAllByReceiverAndStatus(receiver, FriendshipStatus.PENDING)
                .stream()
                .map(FriendShipResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public FriendShipResponseDto updateStatus(String username, FriendshipStatus status, Long friendShipId, String currentUsername) {
        if (!username.equals(currentUsername)) { //현재 경로 유효성을 검증한다.
            throw new CustomException(ErrorCode.ERROR_UNAUTHORIZED);
        }
        User receiver = userService.getUser(username);
        FriendShip friendShip = getFriendShip(friendShipId);
        friendShip.validReceiver(receiver);

        friendShip.updateStatus(status);
        return FriendShipResponseDto.fromEntity(friendShip);
    }

    public FriendShip getFriendShip(Long id) {
        Optional<FriendShip> byId = friendShipRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        throw new CustomException(ErrorCode.FRIEND_SHIP_NOT_FOUND);
    }


    public List<PostResponseDto> getFriendsPost(String username, String currentUsername) {
        if (!username.equals(currentUsername))
            throw new CustomException(ErrorCode.ERROR_UNAUTHORIZED);

        User user = userService.getUser(username);


        List<FriendShip> friendShipByUser = friendShipRepository.findFriendShipByUser(user);

        List<User> friends = friendShipByUser.stream()
                .map(friendShip -> friendShip.getReceiver() == user ? friendShip.getSender() : friendShip.getReceiver())
                .toList();

        return friends.stream()
                .flatMap(u -> u.getPosts().stream())
                .sorted(Comparator.comparingLong(Post::getId).reversed())
                .map(PostResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

}
