package com.isharipov.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Илья on 20.02.2016.
 */
@Controller
public class MainController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView control() {
        String message = "Hello";
        return new ModelAndView("index", "message", message);
    }

    @RequestMapping("/login")
    public ModelAndView loginController() {
        return new ModelAndView("login");
    }
}
