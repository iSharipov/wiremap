package com.isharipov.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Илья on 20.02.2016.
 */
@Slf4j
@Controller
public class MainController {

    @RequestMapping("/")
    public String getHomePage() {
        return "index";
    }


    @RequestMapping("/demo")
    public String getDemoPage() {
        return "demo";
    }

    @RequestMapping("/coverage")
    public String getCoveragePage() {
        return "coverage";
    }
}
