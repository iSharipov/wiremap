package com.isharipov.utils;

import com.isharipov.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by Илья on 05.04.2016.
 */
@Component
public class UserCreateFromValidator implements Validator {

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(UserCreateForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserCreateForm form = (UserCreateForm) target;
    }

    private void validatePasswords(Errors errors, UserCreateForm form) {
        if (!form.getPassword().equals(form.getPasswordRepeated())) {
            errors.reject("password.no.match", "Passwords do not match");
        }
    }

    private void validateEmail(Errors errors, UserCreateForm form) {
        if(userService.getUserByEmail(form.getEmail()).isPresent()){
            errors.reject("email.exists", "User with this email already exists");
        }
    }
}
