package com.movies.movieslist.security.cors;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


public class CorsConfig extends WebMvcConfigurationSupport {

    /*@Bean
    public WebMvcConfigurer getConfiguration() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }*/
}
