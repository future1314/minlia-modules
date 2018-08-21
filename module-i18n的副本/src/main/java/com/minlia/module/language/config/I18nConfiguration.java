package com.minlia.module.language.config;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(value = I18nProperties.class)
@Configuration
public class I18nConfiguration {

}
