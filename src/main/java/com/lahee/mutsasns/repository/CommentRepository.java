package com.lahee.mutsasns.repository;

import com.lahee.mutsasns.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
