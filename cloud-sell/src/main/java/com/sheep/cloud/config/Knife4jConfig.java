package com.sheep.cloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.config
 * @datetime 2022/9/16 星期五
 */
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfig {

    @Bean(value = "dockerBean")
    public Docket dockerBean() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .description("这是knife4j生成的接口文档")
                        .termsOfServiceUrl("http://localhost:7080/")
                        .contact("ssssheep")
                        .version("1.0")
                        .build())
                .groupName("1.0版本")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sheep.cloud.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}
