package com.sheep.cloud;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud
 * @datetime 2022/9/14 星期三
 */
@SpringBootTest(classes = OAuth2Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class DemoApplicationTest {

    @Autowired
    private RedisClient redisClient;

    @Test
    public void contextLoads() {
        jedis.set("test", "test");
    }
}
