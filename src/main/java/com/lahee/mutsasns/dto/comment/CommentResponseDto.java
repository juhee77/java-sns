package com.lahee.mutsasns.dto.comment;

import com.lahee.mutsasns.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private String username;
    private String content;


    public static CommentResponseDto fromEntity(Comment comment) {
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.content = comment.getContent();
        commentResponseDto.username = comment.getUser().getUsername();
        return commentResponseDto;
    }

}
