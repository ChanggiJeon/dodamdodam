package com.ssafy.api.config.jwt;

import com.ssafy.api.entity.User;
import com.ssafy.api.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service("userDetailsService")
public class CustomUserDetailService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String userPk) throws UsernameNotFoundException {
        User user = null;
        try {
            user = userService.findByUserPk(Long.parseLong(userPk));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}