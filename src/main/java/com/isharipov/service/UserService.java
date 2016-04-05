package com.isharipov.service;

import com.isharipov.domain.User;
import com.isharipov.utils.UserCreateForm;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by Илья on 05.04.2016.
 */
public interface UserService {

    Optional<User> getUserById(long id);

    Optional<User> getUserByEmail(String email);

    Collection<User> getAllUsers();

    User create(UserCreateForm form);
}
