package com.isharipov.config.spring;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isharipov.handler.JacksonHandlerInstantiator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Илья on 29.03.2016.
 */
@Configuration
@Import({AsyncConfig.class})
@ComponentScan({"com.isharipov"})
public class AppConfig {

    @Bean
    @Autowired
    public ObjectMapper objectMapper(JacksonHandlerInstantiator jacksonHandlerInstantiator) {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setHandlerInstantiator(jacksonHandlerInstantiator);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }
}
