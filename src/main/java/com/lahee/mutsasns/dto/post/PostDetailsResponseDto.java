package com.lahee.mutsasns.dto.post;

import com.lahee.mutsasns.domain.Post;
import com.lahee.mutsasns.dto.comment.CommentResponseDto;
import com.lahee.mutsasns.dto.file.FileResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

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
                postResponseDto.thumbnail = FileResponseDto.getDefaultPostThumbNail();
                //썸네일이 없고 포스트에는 이미지가 있는 경우 포스트의 제일 첫 이미지 선택
            else postResponseDto.thumbnail = FileResponseDto.fromEntity(post.getPostfiles().get(0));
        } else {
            postResponseDto.thumbnail = FileResponseDto.fromEntity(post.getThumbnail());
        }

        postResponseDto.files = post.getPostfiles()
                .stream().map(FileResponseDto::fromEntity)
                .collect(Collectors.toList());

        postResponseDto.comments = post.getComments()
                .stream()
                .map(CommentResponseDto::fromEntity)
                .collect(Collectors.toList());


        postResponseDto.heartCnt = post.getPostHearts().size();

        return postResponseDto;
    }

}
