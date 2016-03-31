package com.isharipov.handler;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.util.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @since 16.04.2015
 */
@Component
public class JacksonHandlerInstantiator extends HandlerInstantiator {
    @Autowired
    private ApplicationContext springContext;

    @Override
    public JsonDeserializer<?> deserializerInstance(DeserializationConfig config, Annotated annotated, Class deserClass) {
        return null;
    }

    @Override
    public KeyDeserializer keyDeserializerInstance(DeserializationConfig config, Annotated annotated, Class<?> keyDeserClass) {
        return null;
    }

    @Override
    public JsonSerializer<?> serializerInstance(SerializationConfig config, Annotated annotated, Class<?> serClass) {
        return null;
    }

    @Override
    public TypeResolverBuilder<?> typeResolverBuilderInstance(MapperConfig<?> config, Annotated annotated, Class<?> builderClass) {
        return null;
    }

    @Override
    public TypeIdResolver typeIdResolverInstance(MapperConfig<?> config, Annotated annotated, Class<?> resolverClass) {
        return null;
    }

    @Override
    public Converter<?, ?> converterInstance(MapperConfig<?> config,
                                             Annotated annotated, Class<?> implClass) {

        String[] beanNames = springContext.getBeanNamesForType(implClass);

        Converter<?, ?> converter = null;

        if (beanNames.length > 0) {
            converter = (Converter<?, ?>) springContext.getBean(implClass);
        }

// If null, the converter will be created using no arguments constructor
        return converter;
    }
}