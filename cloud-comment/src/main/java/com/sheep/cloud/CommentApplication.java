package com.sheep.cloud;

<<<<<<< HEAD
=======
import org.modelmapper.ModelMapper;
>>>>>>> 116a605 (新增：完成评论角模块)
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
<<<<<<< HEAD
=======
import org.springframework.context.annotation.Bean;
>>>>>>> 116a605 (新增：完成评论角模块)

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud
 * @datetime 2022/8/11 星期四
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class CommentApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommentApplication.class, args);
    }
<<<<<<< HEAD
=======

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
>>>>>>> 116a605 (新增：完成评论角模块)
}
