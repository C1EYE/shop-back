package com.c1eye.server.core.configuration;

import com.c1eye.server.core.interceptors.PermissionInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author c1eye
 * time 2021/10/16 22:03
 */
@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {
    @Bean
    public HandlerInterceptor getPermissionInterceptor() {
        return new PermissionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getPermissionInterceptor());
    }
}
