package com.lahee.mutsasns.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
@Table(name = "user")
@Builder
@SQLDelete(sql = "UPDATE user SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username; //loginId
    private String password;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String phone;

    @OneToOne
    private File image;

    @Builder.Default
    @OneToMany(fetch = LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    @Schema(description = "유저의 게시글")
    private List<Post> posts = new ArrayList<>();

    @Builder.Default //builder로 선언 하는 경우 없는값이라고 null을 넣는다 따라서 이렇게 해야함
    @OneToMany(fetch = LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    @Schema(description = "댓글")
    private List<Comment> comments = new ArrayList<>();

    @Builder.Default //내가 남긴 하트
    @OneToMany(fetch = LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<PostHeart> postHearts = new ArrayList<>();

    @Builder.Default //내가 팔로잉하는 사람
    @OneToMany(fetch = LAZY, mappedBy = "userA", cascade = CascadeType.ALL)
    private List<Following> following = new ArrayList<>();

    @Builder.Default //나의 팔로워
    @OneToMany(fetch = LAZY, mappedBy = "userB", cascade = CascadeType.ALL)
    private List<Following> follower = new ArrayList<>();

    public void updateProfileImage(File file) {
        this.image = file;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
