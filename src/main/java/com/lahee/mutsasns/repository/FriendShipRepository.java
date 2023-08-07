package com.lahee.mutsasns.repository;

import com.lahee.mutsasns.domain.FriendShip;
import com.lahee.mutsasns.domain.FriendshipStatus;
import com.lahee.mutsasns.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendShipRepository extends JpaRepository<FriendShip, Long> {
    List<FriendShip> findAllByReceiverAndStatus(User user, FriendshipStatus status);

    boolean existsByReceiverAndSenderAndStatus(User receiver, User sender, FriendshipStatus status);

    boolean existsBySenderAndReceiverAndStatus(User sender, User receiver, FriendshipStatus status);

    @Query("SELECT f FROM FriendShip f WHERE f.receiver = ?1 OR f.sender = ?1 AND f.status='ACCEPTED'")
    List<FriendShip> findFriendShipByUser(User user);

}
