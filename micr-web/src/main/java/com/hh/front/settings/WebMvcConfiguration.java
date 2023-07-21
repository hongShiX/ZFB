package com.hh.front.settings;

import com.hh.front.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Value("${jwt.secret}")
    private String jwtSecret;
    /**
     * 处理跨域
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        System.out.println("===addCorsMappings===");
        // 处理的请求地址
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:8080")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600)
                .allowedHeaders("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        TokenInterceptor tokenInterceptor = new TokenInterceptor(jwtSecret);
        String[] addPath = {"/v1/user/realname"};
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns(addPath);
    }
}
