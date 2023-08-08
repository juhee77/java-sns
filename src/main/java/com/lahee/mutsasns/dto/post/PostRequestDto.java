package com.lahee.mutsasns.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostRequestDto {
    @Schema(defaultValue = "피드의 제목")
    private String title;
    @Schema(defaultValue = "피드의 본문")
    private String text;
}
