package com.isharipov.config.spring;

import net.sf.ehcache.management.ManagementService;
import org.slf4j.Logger;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;

/**
 * Created by Илья on 02.05.2016.
 */

@Configuration
@EnableCaching
public class CachingConfigurer implements org.springframework.cache.annotation.CachingConfigurer {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(CachingConfigurer.class);

    @Override
    @Bean
    public CacheManager cacheManager() {
        net.sf.ehcache.CacheManager nativeCacheManager = nativeEhCacheManager();
        registerAsMBean(nativeCacheManager);
        EhCacheCacheManager ehcache = new EhCacheCacheManager(nativeCacheManager);
        return ehcache;
    }

    @Override
    public CacheResolver cacheResolver() {
        return null;
    }

    @Override
    public KeyGenerator keyGenerator() {
        return null;
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return null;
    }

    @Bean
    public FactoryBean<net.sf.ehcache.CacheManager> ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean factory = new EhCacheManagerFactoryBean();
        factory.setConfigLocation(new ClassPathResource("ehcache.xml"));
        factory.setShared(true);
        return factory;
    }

    @Bean
    public net.sf.ehcache.CacheManager nativeEhCacheManager() {
        try {
            return ehCacheManagerFactoryBean().getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void registerAsMBean(net.sf.ehcache.CacheManager manager) {
        final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        boolean registerCacheManager = false;
        boolean registerCaches = false;
        boolean registerCacheConfigurations = false;
        boolean registerCacheStatistics = true;

        ManagementService.registerMBeans(manager, mBeanServer, registerCacheManager, registerCaches,
                registerCacheConfigurations, registerCacheStatistics);
    }
}
