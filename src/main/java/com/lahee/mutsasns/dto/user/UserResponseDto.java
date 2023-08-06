package com.lahee.mutsasns.dto.user;


import com.lahee.mutsasns.domain.User;
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

    public static UserResponseDto fromEntity(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.username = user.getUsername();
        userResponseDto.email = user.getEmail();
        userResponseDto.phone = user.getPhone();
        return userResponseDto;
    }
}
