package com.assesment.authentification.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration implements WebMvcConfigurer {
    @Value("${server.port}")
    private int port;

    /**
     * Configuration for Swagger documentation auto generation.
     *
     * @return configured Swagger {@link Docket} instance
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.assesment.authentification.implementation.controller"))
                .paths(PathSelectors.any())
                .build()
                .host(getApiLocation())
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Assigment Portal REST API",
                "",
                "0.0.1",
                "Terms of service",
                new Contact("Alexander Zenkovich", null, "zenkovich.alexsandr@gmail.com"),
                "License of API",
                "API license URL", new ArrayList<>());
    }

    private String getApiLocation() {
        return String.format("%s:%s", "localhost", port);
    }
}
