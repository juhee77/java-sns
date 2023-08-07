package com.lahee.mutsasns.dto.user;


import com.lahee.mutsasns.domain.User;
import com.lahee.mutsasns.dto.file.FileResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private String username;
    private String phone;
    private String email;
    private FileResponseDto profileImage;

    public static UserResponseDto fromEntity(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.username = user.getUsername();
        userResponseDto.email = user.getEmail();
        userResponseDto.phone = user.getPhone();
        if (user.getImage() != null) {
            userResponseDto.profileImage = FileResponseDto.fromEntity(user.getImage());
        } else {
            userResponseDto.profileImage = FileResponseDto.getDefaultUser();
        }

        return userResponseDto;
    }
}
