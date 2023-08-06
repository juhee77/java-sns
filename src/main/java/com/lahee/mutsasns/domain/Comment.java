package com.lahee.mutsasns.domain;

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
@SQLDelete(sql = "UPDATE comment SET deleted_at = datetime('now') WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    private User user; //댓글 작성자

    @ManyToOne(fetch = LAZY)
    private Post post; //댓글이 작성된 글

    private String content; //댓글내용

}
