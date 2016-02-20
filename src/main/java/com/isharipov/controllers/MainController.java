package com.isharipov.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Илья on 20.02.2016.
 */
@Controller
public class MainController {

    @RequestMapping("/")
    public ModelAndView control(){
        String message= "Hello";
        return new ModelAndView("index","message", message);
    }
}
