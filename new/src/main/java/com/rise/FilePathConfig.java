package com.rise;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;




@Configuration
public class FilePathConfig implements WebMvcConfigurer {
    //@Value("C:/video/") //   fileurl      D:/test/
    @Value("${riseout.fileurl}")
    String filelocation;  // 这两个是路径 
    @Value("/static/**") //         /file/**
    String filepath;
    @Value("${riseout.url}")
    String url;
    private static final Logger logger = LoggerFactory.getLogger(FilePathConfig.class);
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //匹配到resourceHandler,将URL映射至location,也就是本地文件夹
        registry.addResourceHandler(filepath).addResourceLocations("file:///" + filelocation);//这里最后一个/不能不写

    }






}
