package com.lahee.mutsasns.util;


import com.lahee.mutsasns.exception.CustomException;
import com.lahee.mutsasns.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
public class SecurityUtil {

    public static String getCurrentUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            log.debug("context에 정보가 없습니다.");
            throw new CustomException(ErrorCode.ERROR_FORBIDDEN);
        }

        String username = null;
        if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            username = springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            username = (String) authentication.getPrincipal();
        }

        return username;
    }
}
