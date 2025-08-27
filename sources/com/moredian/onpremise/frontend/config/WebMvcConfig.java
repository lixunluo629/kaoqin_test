package com.moredian.onpremise.frontend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.support.WebContentGenerator;

@Configuration
/* loaded from: onpremise-frontend-0.0.1-SNAPSHOT.jar:com/moredian/onpremise/frontend/config/WebMvcConfig.class */
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Override // org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter, org.springframework.web.servlet.config.annotation.WebMvcConfigurer
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    }

    @Override // org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter, org.springframework.web.servlet.config.annotation.WebMvcConfigurer
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedHeaders("*").allowedOrigins("*").allowedMethods("GET", WebContentGenerator.METHOD_HEAD, WebContentGenerator.METHOD_POST, "PUT", "PATCH", "DELETE", "OPTIONS", "TRACE").allowCredentials(true);
    }
}
