package com.lahee.mutsasns.service;


import com.lahee.mutsasns.domain.User;
import com.lahee.mutsasns.dto.user.LoginDto;
import com.lahee.mutsasns.dto.user.SignupDto;
import com.lahee.mutsasns.dto.user.TokenDto;
import com.lahee.mutsasns.dto.user.UserResponseDto;
import com.lahee.mutsasns.exception.CustomException;
import com.lahee.mutsasns.exception.ErrorCode;
import com.lahee.mutsasns.jwt.JwtTokenUtils;
import com.lahee.mutsasns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {
    private final JwtTokenUtils jwtTokenUtils;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;

    public TokenDto login(LoginDto loginDto) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getUsername());
        if (!passwordEncoder.matches(loginDto.getPassword(), userDetails.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_PASSWORD);
        }
        TokenDto tokenDto = jwtTokenUtils.generateToken(userDetails);
        return tokenDto;
    }

    @Transactional
    public UserResponseDto signup(SignupDto signupDto) {
        if (userRepository.findByUsername(signupDto.getUsername()).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_USED_USERNAME);
        }

        User user = new User().builder()
                .username(signupDto.getUsername())
                .password(passwordEncoder.encode(signupDto.getPassword()))
                .email(signupDto.getEmail())
                .phone(signupDto.getPhoneNumber())
                .build();

        return UserResponseDto.fromEntity(userRepository.save(user));
    }

    public UserResponseDto getUserDto(String username) {
        return UserResponseDto.fromEntity(getUser(username));
    }

    public User getUser(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND_EXCEPTION);
        }
        return user.get();
    }
}
