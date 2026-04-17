package com.rise;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FilePathConfig implements WebMvcConfigurer {

    @Value("${riseout.fileurl:./upload/}")
    private String staticLocation;

    @Value("${riseout.static-pattern:/static/**}")
    private String staticPattern;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(staticPattern)
                .addResourceLocations(toFileUri(staticLocation));
    }

    private String toFileUri(String location) {
        String normalized = location.replace("\\", "/");
        if (!normalized.endsWith("/")) {
            normalized = normalized + "/";
        }
        if (normalized.startsWith("file:")) {
            return normalized;
        }
        return "file:///" + normalized;
    }
}