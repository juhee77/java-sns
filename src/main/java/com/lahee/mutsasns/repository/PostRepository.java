package com.lahee.mutsasns.repository;

import com.lahee.mutsasns.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
