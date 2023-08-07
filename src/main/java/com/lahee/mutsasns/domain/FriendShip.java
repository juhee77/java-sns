package com.lahee.mutsasns.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import static jakarta.persistence.FetchType.LAZY;

@NoArgsConstructor
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Entity
@ToString
@Table(name = "friend_ship")
@SQLDelete(sql = "UPDATE friend_ship SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
@Schema(description = "친구를 관리한다. ")
public class FriendShip extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "sender")
    private User userA;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "receiver")
    private User userB;

    @Builder.Default
    private FriendshipStatus status = FriendshipStatus.PENDING;

}
