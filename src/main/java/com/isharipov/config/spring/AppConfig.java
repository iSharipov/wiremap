package com.isharipov.config.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isharipov.handler.JacksonHandlerInstantiator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Илья on 29.03.2016.
 */
@Configuration
@ComponentScan({"com.isharipov"})
public class AppConfig {

    @Bean
    @Autowired
    public ObjectMapper objectMapper(JacksonHandlerInstantiator jacksonHandlerInstantiator) {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setHandlerInstantiator(jacksonHandlerInstantiator);
        return mapper;
    }
}
