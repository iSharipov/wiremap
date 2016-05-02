package com.isharipov.config.spring;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isharipov.handler.JacksonHandlerInstantiator;
import com.isharipov.handler.MainResponseErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Илья on 29.03.2016.
 */
@Configuration
@EnableAsync
@Import(CachingConfigurer.class)
@ComponentScan({"com.isharipov"})
public class AppConfig {
    @Bean
    @Autowired
    public ObjectMapper objectMapper(JacksonHandlerInstantiator jacksonHandlerInstantiator) {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.setHandlerInstantiator(jacksonHandlerInstantiator);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return mapper;
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new MainResponseErrorHandler());
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setOutputStreaming(false);
        restTemplate.setRequestFactory(requestFactory);
        restTemplate.getMessageConverters().add(new MappingJackson2XmlHttpMessageConverter());
        return restTemplate;
    }
}