package com.isharipov.service;

import com.isharipov.domain.User;
import com.isharipov.utils.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by Илья on 05.04.2016.
 */
@Service
public class CurrentUserDetailService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public CurrentUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.getUserByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User with email=%s was not found", email)));
        return new CurrentUser(user);
    }
}
