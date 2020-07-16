package com.example.config.redis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @ClassName RedisConfig
 * @Author yupanpan
 * @Date 2019/11/20 13:26
 */
@Configuration
public class RedisConfig {

    protected Logger log = LogManager.getLogger(RedisConfig.class);

    /**
     * 实例化 RedisTemplate 对象代替原有的RedisTemplate<String, String>
     *
     * @return
     */
    @Bean("redisTemplate")
    public RedisTemplate<String, Object> functionDomainRedisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        initDomainRedisTemplate(redisTemplate, redisConnectionFactory);
        return redisTemplate;
    }

    /**
     * 设置数据存入 redis 的序列化方式
     *
     * @param redisTemplate
     * @param factory
     */
    private void initDomainRedisTemplate(RedisTemplate<String, Object> redisTemplate, RedisConnectionFactory factory) {
        // 如果不配置Serializer，那么存储的时候缺省使用String，比如如果用User类型存储，那么会提示错误User can't cast
        // to String！
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // 开启事务/true必须手动释放连接，false会自动释放连接 如果调用方有用@Transactional做事务控制，可以开启事务,Spring会处理连接问题
        redisTemplate.setEnableTransactionSupport(false);
        redisTemplate.setConnectionFactory(factory);
    }
}
