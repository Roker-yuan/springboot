package com.roker.springbootschedulesinglepoint.redis;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.nio.charset.StandardCharsets;

/**
 * 自定义监听器.
*/
public class KeyExpiredListener extends KeyExpirationEventMessageListener {
    public KeyExpiredListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // channel
        String channel = new String(message.getChannel(), StandardCharsets.UTF_8);
        // 过期的key
        String key = new String(message.getBody(), StandardCharsets.UTF_8);
        // todo 你的处理
    }
}
