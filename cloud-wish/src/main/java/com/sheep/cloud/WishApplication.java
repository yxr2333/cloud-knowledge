package com.sheep.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

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
public class WishApplication {
    public static void main(String[] args) {
        SpringApplication.run(WishApplication.class, args);
    }
}

