package com.lahee.mutsasns.repository;

import com.lahee.mutsasns.domain.Post;
import com.lahee.mutsasns.domain.PostHeart;
import com.lahee.mutsasns.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostHeartRepository extends JpaRepository<PostHeart, Long> {
    boolean existsByUserAndPost(User user, Post post);

    void deleteByUserAndPost(User user, Post post);
}
