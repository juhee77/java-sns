package com.lahee.mutsasns.dto.post;

import com.lahee.mutsasns.domain.File;
import com.lahee.mutsasns.domain.Post;
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
public class PostResponseDto {
    private static final String DEFAULT_IMAGE_PATH = "/static/default-image.png";

    private Long id;
    private String title;
    private String text;
    private List<String> files;

    public static PostResponseDto fromEntity(Post post) {
        PostResponseDto postResponseDto = new PostResponseDto();
        postResponseDto.id = post.getId();
        postResponseDto.text = post.getText();
        postResponseDto.title = post.getTitle();

        List<String> tempFiles = new ArrayList<>();
        if (post.getPostfiles().isEmpty()) {
            tempFiles.add(DEFAULT_IMAGE_PATH);
        }else{
            for (File postfile : post.getPostfiles()) {
                tempFiles.add(postfile.getStorePath());
            }
            postResponseDto.files = tempFiles;
        }
        return postResponseDto;
    }

}
