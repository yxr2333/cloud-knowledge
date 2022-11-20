package com.sheep.cloud;

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
 * @package com.sheep.cloud
 * @datetime 2022/8/11 星期四
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@EntityScan(basePackages = {"com.sheep.cloud.entity.knowledge"})
@EnableJpaRepositories(basePackages = {"com.sheep.cloud.dao.knowledge"})

public class WishApplication {
    public static void main(String[] args) {
        SpringApplication.run(WishApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}

