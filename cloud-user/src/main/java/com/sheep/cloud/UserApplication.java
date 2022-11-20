package com.sheep.cloud;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud
 * @datetime 2022/8/11 星期四
 */
@SpringBootApplication
//@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
@EntityScan(basePackages = {"com.sheep.cloud.entity.knowledge"})
@EnableJpaRepositories(basePackages = {"com.sheep.cloud.dao.knowledge"})
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }


    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public COSClient cosClient() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("cos.properties");
        InputStream stream = classPathResource.getInputStream();
        Properties properties = new Properties();
        properties.load(stream);
        String secretId = properties.getProperty("secretId");
        String secretKey = properties.getProperty("secretKey");
        BasicCOSCredentials credentials = new BasicCOSCredentials(secretId, secretKey);
        Region region = new Region("ap-shanghai");
        ClientConfig clientConfig = new ClientConfig(region);
        clientConfig.setHttpProtocol(HttpProtocol.https);
        return new COSClient(credentials, clientConfig);
    }

}
