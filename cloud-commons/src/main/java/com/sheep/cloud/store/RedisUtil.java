package com.sheep.cloud.store;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.store
 * @datetime 2022/9/14 星期三
 */
@Component
@Slf4j
public class RedisUtil {
    private static final String IP = "localhost";

    private static final Integer PORT = 6379;

    public Jedis jedis = null;

    @PostConstruct
    public void initJedis() {
        jedis = new Jedis(IP, PORT);
    }

    public void set(String key, String value) {
        jedis.set(key, value);
    }

    public String get(String key) {
        return jedis.get(key);
    }

}
