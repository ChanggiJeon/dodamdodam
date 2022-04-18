/**
 * Created by DominikH on 24.04.2017.
 */
package com.ssafy.dodamdodam.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;


@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private String version;
    private String title;

    @Value("${swagger.host}")
    private String host;

    @Bean
    public Docket apiV1() {
        version = "v1.00";
        title = "DodamDodam" + version;

        return new Docket(DocumentationType.SWAGGER_2)
                .host(host)
                .useDefaultResponseMessages(false)
                .groupName(version)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ssafy.dodamdodam.controller"))
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .apiInfo(apiInfo(title, version));

    }

    private ApiInfo apiInfo(String title, String version) {
        return new ApiInfo(
                title,
                "DodamDodam API",
                version,
                "www.example.com",
                new Contact("Contact Me", "www.example.com", "test@example.com"),
                "Licenses",
                "www.example.com",
                new ArrayList<>());
    }

}
