package com.lahee.mutsasns.dto.friendShip;

import com.lahee.mutsasns.domain.FriendShip;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendShipResponseDto {
    private Long id;
    private String sender;
    private String receiver;
    private String status;

    public static FriendShipResponseDto fromEntity(FriendShip friendShip) {
        FriendShipResponseDto friendShipDto = new FriendShipResponseDto();
        friendShipDto.id = friendShip.getId();
        friendShipDto.receiver =friendShip.getReceiver().getUsername();
        friendShipDto.sender =friendShip.getSender().getUsername();
        friendShipDto.status = friendShip.getStatus().getName();

        return friendShipDto;
    }

}
