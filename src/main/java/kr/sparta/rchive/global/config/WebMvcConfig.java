package kr.sparta.rchive.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3000",
                        "http://rchive.kr", "http://www.rchive.kr",
                        "http://office.rchive.kr", "http://dev.rchive.kr",
                        "https://rchive.kr", "https://www.rchive.kr",
                        "https://office.rchive.kr", "https://dev.rchive.kr")
                .allowedMethods("*")
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .allowCredentials(true)
                .maxAge(3600);
    }

}