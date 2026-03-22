package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // applies to all endpoints
                .allowedOrigins("http://localhost:3000") // allow frontend origin
                .allowedMethods("GET", "POST", "PUT", "DELETE") // allow common HTTP methods
                .allowedHeaders("*"); // allow all headers
    }
}
