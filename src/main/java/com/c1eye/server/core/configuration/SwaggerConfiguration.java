package com.c1eye.server.core.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * author c1eye
 * time 2021/9/29 10:46
 */


//FIXME 配置了自动路径映射swagger用不了了
//@EnableSwagger2
//@Configuration
public class SwaggerConfiguration {
    @Value("${c1eye.api-package}")
    private String basePackageName;
    @Bean
    Docket webApiConfig(){
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).groupName("A").select()
                                           .apis(RequestHandlerSelectors.basePackage(basePackageName)).build();
    }

    ApiInfo apiInfo(){
        return new ApiInfoBuilder().title("").description("").
                version("").contact(new Contact("a", "A", "A"))
                            .build();
    }
}
