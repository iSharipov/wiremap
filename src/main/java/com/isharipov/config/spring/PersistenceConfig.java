package com.isharipov.config.spring;

import com.isharipov.handler.DatabaseErrorHandler;
import com.isharipov.utils.FileUtils;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;

/**
 * Created by Илья on 20.02.2016.
 */
@Configuration
@ComponentScan("com.isharipov")
@EnableJpaRepositories("com.isharipov.repository")
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class PersistenceConfig {

    @Value("${jdbc.driverClassName}")
    private String databaseDriverClassName;

    @Value("${jdbc.url}")
    private String jdbcUrl;

    @Value("${jdbc.username}")
    private String jdbcUsername;

    @Value("${jdbc.password}")
    private String jdbcPassword;

    @Value("${jdbc.reconnect.max_tries_count}")
    private Integer maxTriesCount;

    @Value("${jdbc.reconnect.timeout}")
    private Integer reconnectionTimeout;

    @Bean
    public DataSource dataSource() {
        try {
            ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
            comboPooledDataSource.setDriverClass(databaseDriverClassName);
            comboPooledDataSource.setJdbcUrl(jdbcUrl);
            comboPooledDataSource.setUser(jdbcUsername);
            comboPooledDataSource.setPassword(jdbcPassword);
            comboPooledDataSource.setAcquireIncrement(5);
            comboPooledDataSource.setIdleConnectionTestPeriod(60);
            comboPooledDataSource.setMaxPoolSize(100);
            comboPooledDataSource.setMaxStatements(50);
            comboPooledDataSource.setMinPoolSize(10);
            return comboPooledDataSource;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    @Autowired
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) throws IOException {
        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(dataSource);
        emf.setPackagesToScan("com.isharipov.domain");
        emf.setPersistenceUnitName("txManager");
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        emf.setJpaProperties(FileUtils.loadFileProperties(new ClassPathResource("hibernate.properties").getPath()));
        emf.setJpaVendorAdapter(vendorAdapter);
        return emf;
    }

    @Bean
    @Autowired
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        final JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslationServiceBus() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public DatabaseErrorHandler databaseErrorHandler() {
        DatabaseErrorHandler databaseErrorHandler = new DatabaseErrorHandler();
        databaseErrorHandler.setMaxTriesCount(maxTriesCount);
        databaseErrorHandler.setReconnectionTimeout(reconnectionTimeout);
        databaseErrorHandler.setTxManagerId("transactionManager");
        return databaseErrorHandler;
    }
}
