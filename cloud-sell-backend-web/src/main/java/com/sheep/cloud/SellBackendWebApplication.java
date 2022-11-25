package com.sheep.cloud;

import com.sheep.cloud.config.CustomLoginConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import xyz.erupt.core.annotation.EruptScan;
import xyz.erupt.upms.fun.EruptLogin;

/**
 * Created By IntelliJ IDEA
 *
 * @author IceCreamQAQ
 * @datetime 2022/11/24 星期四
 * Happy Every Coding Time~
 */

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EruptLogin(CustomLoginConfig.class)
@EntityScan(basePackages = {"com.sheep.cloud.entity.sell", "xyz.erupt.**.**"})
@EnableJpaRepositories(basePackages = {"com.sheep.cloud.dao.sell", "xyz.erupt.**.**"})
@EruptScan(value = {"com.sheep.cloud.entity.sell", "xyz.erupt.**.**"})
public class SellBackendWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(SellBackendWebApplication.class, args);
    }
}
