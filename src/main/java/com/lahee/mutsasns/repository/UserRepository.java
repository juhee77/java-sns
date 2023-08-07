package com.lahee.mutsasns.repository;

import com.lahee.mutsasns.domain.Post;
import com.lahee.mutsasns.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String userName);

    @Query("SELECT DISTINCT posts FROM User u " +
            "JOIN u.following followingUser " +
            "JOIN followingUser.following p " +
            "JOIN p.posts posts WHERE u.username =:username"
    )
    List<Post> findPostsByFollowingUsers(String username);

}
