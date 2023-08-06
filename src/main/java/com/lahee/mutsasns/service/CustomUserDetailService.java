package com.lahee.mutsasns.service;


import com.lahee.mutsasns.exception.CustomException;
import com.lahee.mutsasns.exception.ErrorCode;
import com.lahee.mutsasns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String name) {
        Optional<com.lahee.mutsasns.domain.User> findedUser = userRepository.findByUsername(name);
        Optional<User> user = findedUser.map(this::createUser);

        if (!findedUser.isPresent()) {
            throw new CustomException(ErrorCode.ERROR_CONFLICT);
        }

        return user.get();
    }

    private org.springframework.security.core.userdetails.User createUser(com.lahee.mutsasns.domain.User user) {
        log.info("created security user");
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

}
