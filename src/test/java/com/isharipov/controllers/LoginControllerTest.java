package com.isharipov.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * Created by Илья on 08.04.2016.
 */

public class LoginControllerTest {

    @Test
    public void testLoginController() {
        LoginController loginController = new LoginController();

        ModelAndView viewName = loginController.getLoginPage(Optional.of(""));
        assertEquals("login", viewName.getViewName());
    }
}
