package com.example.config.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * @ClassName RedissonConfig  主要用于集群/分布式锁等控制
 * @Author yupanpan
 * @Date 2019/10/21 9:27
 */
@Configuration
public class RedissonConfig {

    private static final Logger logger = LoggerFactory.getLogger(RedissonConfig.class);

    @Bean
    public RedissonClient redissonClient() {
        RedissonClient redisson = null;
        try {
            Config config = Config.fromYAML(new ClassPathResource("redis/redisson.yml").getInputStream());
            redisson = Redisson.create(config);
        } catch (Exception e) {
            logger.error("初始化redisson失败", e);
        }
        return redisson;
    }
}
