package com.lahee.mutsasns.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
    String message;

    public static MessageResponse getInstance(String deletedCommentMsg) {
        return new MessageResponse(deletedCommentMsg);
    }

}
