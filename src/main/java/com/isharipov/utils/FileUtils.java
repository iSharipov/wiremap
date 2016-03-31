package com.isharipov.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Properties;

public class FileUtils {
    public FileUtils() {

    }

    public static Properties loadFileProperties(String file) throws IOException {
        Properties properties = new Properties();
        ClassPathResource propertiesResource = new ClassPathResource(file);
        properties.load(propertiesResource.getInputStream());
        return properties;
    }
}