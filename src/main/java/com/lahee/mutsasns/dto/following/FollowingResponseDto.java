package com.lahee.mutsasns.dto.following;

import com.lahee.mutsasns.domain.Following;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowingResponseDto {
    String following;
    String follower;

    public static FollowingResponseDto fromEntity(Following following) {
        FollowingResponseDto followingResponseDto = new FollowingResponseDto();
        followingResponseDto.follower = following.getFollower().getUsername();
        followingResponseDto.following = following.getFollowing().getUsername();
        return followingResponseDto;
    }

}
