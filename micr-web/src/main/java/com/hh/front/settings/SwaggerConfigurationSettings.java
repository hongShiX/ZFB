package com.hh.front.settings;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfigurationSettings {
    // 创建Docket对象
    @Bean
    public Docket docket() {
        // 创建Doclet对象
        Docket docket = new Docket(DocumentationType.SWAGGER_2);

        // 创建api信息，接口文档的总体描述
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("智付宝")
                .version("1.0")
                .description("前后端分离的金融项目,前端Vue，后端Springboot+dubbo")
                .contact(new Contact("HH公司", "http://www.hh.com", "litangdingzhen2024@163.com"))
                .license("Apache2.0")
                .build();

        // 设置使用apiInfo
        docket = docket.apiInfo(apiInfo);

        // 设置参与文档生成的包
        docket = docket.select().
                apis(RequestHandlerSelectors.basePackage("com.hh.front.controller")).build();

        // 返回Docket对象

        return docket;
    }
}
