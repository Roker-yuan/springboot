package com.roker.redis;

import com.roker.redis.model.UserVo;
import com.roker.redis.util.JacksonUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringbootRedisApplication.class)
public class RedisLockTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Test
    public void test(){
        //分布式自增ID
        for (int i = 0; i < 10; i++) {
            Long incrementId = stringRedisTemplate.opsForValue().increment("orderId");
            //设置2秒后自动过期
            stringRedisTemplate.expire("orderId", 2, TimeUnit.SECONDS);
            System.out.println("orderId当前值：" +  incrementId);
        }

        //分布式加锁，5秒自动过期
        boolean lock = lock("LOCK", "test", 5L);
        System.out.println("加锁结果：" +  lock);
        boolean unlock = releaseLock("LOCK", "test");
        System.out.println("解锁结果：" +  unlock);
    }



    /**
     * 直接加锁
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public boolean lock(String key,String value, Long expire){
        String luaScript = "if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then return redis.call('expire', KEYS[1], ARGV[2]) else return 0 end";
        RedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);
        Long result = stringRedisTemplate.execute(redisScript, Collections.singletonList(key), value, String.valueOf(expire));
        return result.equals(Long.valueOf(1));
    }


    /**
     * 释放锁
     * @param key
     * @param value
     * @return
     */
    public boolean releaseLock(String key,String value){
        String luaScript = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        RedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);
        Long result = stringRedisTemplate.execute(redisScript, Collections.singletonList(key),value);
        return result.equals(Long.valueOf(1));
    }



}
