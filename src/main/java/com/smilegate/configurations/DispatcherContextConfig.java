package com.smilegate.configurations;

import org.springframework.context.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@ComponentScan(
        basePackages = { "com.smilegate.entrypoints" },
        includeFilters = { @ComponentScan.Filter(Controller.class) },
        useDefaultFilters = false
)
@PropertySource(value = "classpath:application.properties")
public class DispatcherContextConfig extends WebMvcConfigurationSupport {
}
