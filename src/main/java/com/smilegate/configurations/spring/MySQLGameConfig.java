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
        basePackages={"com.smilegate.dataproviders.database.mysql.game"},
        transactionManagerRef="gameTransactionManager",
        entityManagerFactoryRef = "gameEntityManagerFactory"
)
public class MySQLGameConfig {

    @Bean(name = "gameDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.hikari.game")
    public DataSource gameDataSource(){
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }


    @Bean(name="gameEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean gameEntityManagerFactoryBean(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(gameDataSource())
                .packages("com.smilegate.dataproviders.database.mysql.game")
                .persistenceUnit("GameUnit")
                .build();
    }


    @Bean(name="gameTransactionManager")
    public PlatformTransactionManager gameTransactionManager(EntityManagerFactoryBuilder builder){
        return new JpaTransactionManager(gameEntityManagerFactoryBean(builder).getObject());
    }
}
