package com.isharipov.utils;


import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

/**
 * Created by Илья on 05.04.2016.
 */
public class CurrentUser extends User {

    private com.isharipov.domain.User user;

    public CurrentUser(com.isharipov.domain.User user) {
        super(user.getEmail(), user.getPasswordHash(), AuthorityUtils.createAuthorityList(user.getRole().toString()));
        this.user = user;
    }

    public com.isharipov.domain.User getUser() {
        return user;
    }

    public Long getId() {
        return user.getId();
    }

    public Role getRole() {
        return user.getRole();
    }
}
