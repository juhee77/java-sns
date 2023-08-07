package com.lahee.mutsasns.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
@Table(name = "following")
@SQLDelete(sql = "UPDATE following SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@Schema(description = "팔로우를 관리한다 ") //A->B를 팔로우한다.
public class Following extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "follower_id")
    private User userA;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "following_id")
    private User userB;

}
