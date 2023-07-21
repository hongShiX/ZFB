package com.hh.front;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.hh.common.util.JwtUtil;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableDubbo // 启动dubbo服务
//启动swagger ui
@EnableSwagger2
@EnableSwaggerBootstrapUI

@SpringBootApplication
public class MicrWebApplication {
    @Value("${jwt.secret}")
    private String secretKey;
    // 创建JwtUtil对象
    @Bean
    public JwtUtil jwtUtil() {
        JwtUtil jwtUtil = new JwtUtil(secretKey);
        return jwtUtil;
    };

    public static void main(String[] args) {
        SpringApplication.run(MicrWebApplication.class, args);
    }

}
