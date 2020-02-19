package com.smilegate.configurations;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;

@Configuration
@ComponentScan(
        basePackages = { "com.smilegate" },
        excludeFilters = {
                @ComponentScan.Filter(Controller.class)
        }
)
@EntityScan(
        // java8 localdatetime 을 jpa에 적용하기위한 convert 
        basePackageClasses = {Jsr310JpaConverters.class},
        basePackages = {"com.smilegate.dataproviders"})
@EnableAspectJAutoProxy
@EnableAsync
@EnableEncryptableProperties
@PropertySource(value = "classpath:application.properties")
public class RootContextConfig {
}
