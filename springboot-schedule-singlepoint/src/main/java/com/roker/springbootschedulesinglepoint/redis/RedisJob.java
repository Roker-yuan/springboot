package com.roker.springbootschedulesinglepoint.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.util.Set;

/**
 * Description: 基于Redis的ZSet的定时任务
 * 基于 Redis 的定时任务能够适用的场景也比较有限，但实现上相对简单，但对于功能幂等有很大要求。从使用场景上来说，更应该叫做延时任务。
 * 适用场景如下：
 * 订单下单之后 15 分钟后，用户如果没有付钱，系统需要自动取消订单
 * 红包 24 小时未被查收，需要延迟执退还业务
 * 某个活动指定在某个时间内生效&失效
 * 优劣势是：
 * 省去了 MySQL 的查询操作，而使用性能更高的 Redis 做为代替
 * 不会因为停机等原因，遗漏要执行的任务
 * 键空间通知的方式：我们可以通过 Redis 的键空间通知来实现定时任务，它的实现思路是给所有的定时任务设置一个过期时间，等到了过期之后，我们通过订阅过期消息就能感知到定时任务需要被执行了，此时我们执行定时任务即可。
 * 默认情况下 Redis 是不开启键空间通知的，需要我们通过 config set notify-keyspace-events Ex 的命令手动开启。
 * 被动触发，对于服务的资源消耗更小
 * Redis 的 Pub/Sub 不可靠，没有 ACK 机制等，但是一般情况可以容忍
 * 键空间通知功能会耗费一些 CPU
 */
@Configuration
@EnableScheduling
public class RedisJob {
    public static final String JOB_KEY = "redis.job.task";
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisJob.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 添加任务.
     *
     * @param task
     */
    public void addTask(String task, Instant instant) {
        stringRedisTemplate.opsForZSet().add(JOB_KEY, task, instant.getEpochSecond());
    }

    /**
     * 定时任务队列消费
     * 每分钟消费一次（可以缩短间隔到1s）
     */
    @Scheduled(cron = "0 0/1 * * * ? *")
    public void doDelayQueue() {
        long nowSecond = Instant.now().getEpochSecond();
        // 查询当前时间的所有任务
        Set<String> strings = stringRedisTemplate.opsForZSet().range(JOB_KEY, 0, nowSecond);
        for (String task : strings) {
            // 开始消费 task
            LOGGER.info("执行任务:{}", task);
        }
        // 删除已经执行的任务
        stringRedisTemplate.opsForZSet().remove(JOB_KEY, 0, nowSecond);
    }
}
