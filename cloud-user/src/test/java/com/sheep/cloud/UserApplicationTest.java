package com.sheep.cloud;

import com.sheep.cloud.dao.knowledge.IUsersEntityRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Collectors;

/**
 * Created By IntelliJ IDEA
 *
 * @author IceCreamQAQ
 * @datetime 2023/02/22 星期三
 * Happy Every Coding Time~
 */
@SpringBootTest
public class UserApplicationTest {

    @Autowired
    private IUsersEntityRepository userRepository;

    @Test
    public void contextLoads() {
        userRepository
                .findAll()
                .stream()
                .filter(item -> item.getUsername().length() == 3)
                .collect(Collectors.toList())
                .forEach(System.out::println);
    }
}
