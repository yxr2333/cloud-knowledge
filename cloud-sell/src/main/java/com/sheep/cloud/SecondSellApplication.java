package com.sheep.cloud;

import com.sheep.cloud.entity.sell.ISellUserRoleEntity;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.cloud.sheep
 * @datetime 2022/9/15 星期四
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EntityScan(basePackages = {"com.sheep.cloud.entity.sell"})
@EnableJpaRepositories(basePackages = {"com.sheep.cloud.dao.sell"})
public class SecondSellApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecondSellApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean(name = "normalUser", value = "normalUser")
    public ISellUserRoleEntity normalUser() {
        return ISellUserRoleEntity.builder()
                .id(1)
                .name("normal_user")
                .build();
    }

    @Bean(name = "superAdmin", value = "superAdmin")
    public ISellUserRoleEntity superAdmin() {
        return ISellUserRoleEntity.builder()
                .id(2)
                .name("super_admin")
                .build();
    }
}
