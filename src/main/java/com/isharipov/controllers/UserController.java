package com.isharipov.controllers;

import com.isharipov.service.UserService;
import com.isharipov.utils.UserCreateForm;
import com.isharipov.utils.UserCreateFromValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.NoSuchElementException;

/**
 * Created by Илья on 05.04.2016.
 */
@Slf4j
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserCreateFromValidator validator;

    @InitBinder("form")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @RequestMapping("/user/{id}")
    public ModelAndView getUserPage(@PathVariable Long id) {
        return new ModelAndView("user", "user", userService.getUserById(id)
                .orElseThrow(
                        () -> new NoSuchElementException(
                                String.format("User=%s not found", id))
                ));
    }

    @RequestMapping(value = "/user/create", method = RequestMethod.GET)
    public ModelAndView getUserCreatePage() {
        return new ModelAndView("user_create", "form", new UserCreateForm());
    }

    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public String handleUserCreateForm(@Valid @ModelAttribute("form") UserCreateForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "create_form";
        }
        try {
            userService.create(form);
            log.info("Create user with email: {}", form.getEmail());
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            bindingResult.reject("email.exists", "Email already exists");
            return "user_create";
        }
        return "redirect:/";
    }
}
