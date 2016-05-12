package com.isharipov.config.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by Илья on 25.03.2016.
 */
@Configuration
@ComponentScan("com.isharipov")
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

   @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/demo").authenticated().and()
                .formLogin()
                .loginPage("/login").failureUrl("/login?error")
                .usernameParameter("username")
                .passwordParameter("password")
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/")
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

    public void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService)
        .passwordEncoder(new BCryptPasswordEncoder());
    }
}
