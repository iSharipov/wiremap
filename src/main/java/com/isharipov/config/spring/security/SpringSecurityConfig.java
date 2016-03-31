package com.isharipov.config.spring.security;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by Илья on 25.03.2016.
 */
@Configuration
@ComponentScan("com.isharipov")
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {


   @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/").authenticated().and()
                .formLogin()
                .loginPage("/login").failureUrl("/login?error")
                .usernameParameter("username")
                .passwordParameter("password")
                .and()
                .logout().logoutSuccessUrl("/index")
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }
}
