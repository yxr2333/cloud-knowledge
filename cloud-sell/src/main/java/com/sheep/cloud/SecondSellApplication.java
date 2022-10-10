package com.sheep.cloud;

import com.sheep.cloud.model.IUserRoleEntity;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.cloud.sheep
 * @datetime 2022/9/15 星期四
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class SecondSellApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecondSellApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean(name = "normalUser", value = "normalUser")
    public IUserRoleEntity normalUser() {
        return IUserRoleEntity.builder()
                .id(1)
                .name("normal_user")
                .build();
    }

    @Bean(name = "superAdmin", value = "superAdmin")
    public IUserRoleEntity superAdmin() {
        return IUserRoleEntity.builder()
                .id(2)
                .name("super_admin")
                .build();
    }
}
