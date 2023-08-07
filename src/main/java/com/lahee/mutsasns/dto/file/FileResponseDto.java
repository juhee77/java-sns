package com.lahee.mutsasns.dto.file;

import com.lahee.mutsasns.domain.File;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.lahee.mutsasns.constants.constants.DEFAULT_IMAGE_PATH;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileResponseDto {
    private String storePath;

    public static FileResponseDto getDefault() {
        return new FileResponseDto(DEFAULT_IMAGE_PATH);
    }

    public static FileResponseDto fromEntity(File file) {
        return new FileResponseDto(file.getStorePath());
    }

}
