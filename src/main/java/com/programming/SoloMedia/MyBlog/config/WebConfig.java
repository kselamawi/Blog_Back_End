//package com.programming.SoloMedia.MyBlog.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//@EnableWebMvc
//public class WebConfig implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry corsRegistry){
//        corsRegistry.addMapping("/**")
//                .allowedOrigins("http://localhost:4200")
//                .allowedMethods("GET,POST,DELETE,HEADER,PUT")
//                .maxAge(3600L)
//                .allowedHeaders("Access-Control-Allow-Origin:http://localhost:4200")
//                .allowedHeaders("Access-Control-Allow-Credentials: true")
//                .exposedHeaders("Authorization")
//                .allowCredentials(true);
//    }
//}
