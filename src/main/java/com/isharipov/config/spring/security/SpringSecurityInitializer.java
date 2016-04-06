package com.isharipov.config.spring.security;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * Created by Илья on 26.03.2016.
 */
@Order(2)
public class SpringSecurityInitializer extends AbstractSecurityWebApplicationInitializer {

}
