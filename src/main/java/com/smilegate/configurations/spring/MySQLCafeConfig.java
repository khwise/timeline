package com.smilegate.configurations.spring;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties
@EnableJpaRepositories(
        basePackages={"com.smilegate.dataproviders.database.mysql.cafe"},
        transactionManagerRef="cafeTransactionManager",
        entityManagerFactoryRef = "cafeEntityManagerFactory"
)
public class MySQLCafeConfig {

    @Primary
    @Bean(name = "cafeDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.cafe")
    public DataSource cafeDataSource(){
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean(name="cafeEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean cafeEntityManagerFactoryBean(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(cafeDataSource())
                .packages("com.smilegate.dataproviders.database.mysql.cafe")
                .persistenceUnit("CafeUnit")
                .build();
    }

    @Primary
    @Bean(name="cafeTransactionManager")
    public PlatformTransactionManager cafeTransactionManager(EntityManagerFactoryBuilder builder){
        return new JpaTransactionManager(cafeEntityManagerFactoryBean(builder).getObject());
    }
}
