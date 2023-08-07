package com.lahee.mutsasns.dto.file;

import com.lahee.mutsasns.domain.File;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.lahee.mutsasns.constants.constants.DEFAULT_THUMBNAIL_IMAGE_PATH;
import static com.lahee.mutsasns.constants.constants.DEFAULT_USER_IMAGE_PATH;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileResponseDto {
    private String storePath;

    public static FileResponseDto getDefaultUser() {
        return new FileResponseDto(DEFAULT_USER_IMAGE_PATH);
    }

    public static FileResponseDto getDefaultPostThumbNail() {
        return new FileResponseDto(DEFAULT_THUMBNAIL_IMAGE_PATH);
    }

    public static FileResponseDto fromEntity(File file) {
        return new FileResponseDto(file.getStorePath());
    }

}
