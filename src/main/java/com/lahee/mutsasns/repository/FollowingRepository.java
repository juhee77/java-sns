package com.lahee.mutsasns.repository;

import com.lahee.mutsasns.domain.Following;
import com.lahee.mutsasns.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowingRepository extends JpaRepository<Following, Long> {
    boolean existsByFollowerAndFollowing(User follower, User following);

    void deleteByFollowerAndFollowing(User follower, User following);
}
