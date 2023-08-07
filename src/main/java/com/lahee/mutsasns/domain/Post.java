package com.lahee.mutsasns.domain;


import com.lahee.mutsasns.dto.post.PostRequestDto;
import com.lahee.mutsasns.exception.CustomException;
import com.lahee.mutsasns.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
@Table(name = "post")
@SQLDelete(sql = "UPDATE post SET deleted_at = datetime('now') WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id") //게시글의 Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; //게시글 작성자

    private String title;
    private String text;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Comment> comments = new ArrayList<>(); //게시물의 댓글

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PostHeart> postHearts = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<File> postfiles = new ArrayList<>();

    @OneToOne
    private File thumbnail;

    public static Post getEntityInstance(PostRequestDto postRequestDto, User user) {
        return Post.builder()
                .title(postRequestDto.getTitle())
                .text(postRequestDto.getText())
                .user(user)
                .build();
    }

    public void uploadFiles(List<File> files) {
        postfiles = files;
    }

    public void uploadThumbnail(File file) {
        thumbnail = file;
    }

    public void validUser(User user) {
        if (!this.user.equals(user)) {
            throw new CustomException(ErrorCode.ERROR_FORBIDDEN);
        }
    }
}
