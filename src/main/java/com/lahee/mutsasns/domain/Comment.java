package com.lahee.mutsasns.domain;

import com.lahee.mutsasns.dto.comment.CommentRequestDto;
import com.lahee.mutsasns.dto.post.PostRequestDto;
import com.lahee.mutsasns.exception.CustomException;
import com.lahee.mutsasns.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import static jakarta.persistence.FetchType.LAZY;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@Entity
@Schema(description = "댓글")
@Table(name = "comment")
@SQLDelete(sql = "UPDATE comment SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    private User user; //댓글 작성자

    @ManyToOne(fetch = LAZY)
    private Post post; //댓글이 작성된 글

    private String content; //댓글내용


    public void validUser(User user) {
        if (!this.user.equals(user)) {
            throw new CustomException(ErrorCode.ERROR_UNAUTHORIZED);
        }
    }

    public void validPost(Post post) {
        if (!this.post.equals(post)) {
            throw new CustomException(ErrorCode.ERROR_BAD_REQUEST);
        }
    }

    public static Comment getEntityInstance(CommentRequestDto commentRequestDto, User user, Post post) {
        return Comment.builder()
                .content(commentRequestDto.getContent())
                .user(user)
                .post(post)
                .build();
    }

    public void update(CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContent();
    }
}
