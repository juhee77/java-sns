package com.lahee.mutsasns.dto.post;

import com.lahee.mutsasns.domain.Comment;
import com.lahee.mutsasns.domain.File;
import com.lahee.mutsasns.domain.Post;
import com.lahee.mutsasns.dto.comment.CommentResponseDto;
import com.lahee.mutsasns.dto.file.FileResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDetailsResponseDto {

    private Long id;
    private String title;
    private String text;
    private String username;
    private FileResponseDto thumbnail;
    private List<FileResponseDto> files;
    private List<CommentResponseDto> comments;
    private int heartCnt;

    public static PostDetailsResponseDto fromEntity(Post post) {
        PostDetailsResponseDto postResponseDto = new PostDetailsResponseDto();
        postResponseDto.id = post.getId();
        postResponseDto.text = post.getText();
        postResponseDto.title = post.getTitle();
        postResponseDto.username = post.getUser().getUsername();

        if (post.getThumbnail() == null) {
            //썸네일이 없고 포스트에 이미지도 없는 경우
            if (post.getPostfiles() == null || post.getPostfiles().isEmpty())
                postResponseDto.thumbnail = FileResponseDto.getDefault();
                //썸네일이 없고 포스트에는 이미지가 있는 경우 포스트의 제일 첫 이미지 선택
            else postResponseDto.thumbnail = FileResponseDto.fromEntity(post.getPostfiles().get(0));
        } else {
            postResponseDto.thumbnail = FileResponseDto.fromEntity(post.getThumbnail());
        }

        List<FileResponseDto> tempFiles = new ArrayList<>();
        for (File postfile : post.getPostfiles()) {
            tempFiles.add(FileResponseDto.fromEntity(postfile));
        }
        postResponseDto.files = tempFiles;

        List<CommentResponseDto> tempComments = new ArrayList<>();
        for (Comment comment : post.getComments()) {
            tempComments.add(CommentResponseDto.fromEntity(comment));
        }
        postResponseDto.comments = tempComments;

        postResponseDto.heartCnt = post.getPostHearts().size();

        return postResponseDto;
    }

}