package com.mystater.mycache.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import redis.clients.jedis.Jedis;

/**
 * @Description:
 * @author: 西门
 * @Date: 2019/6/5
 * @version: 1.0.0
 */
@EnableConfigurationProperties(RedisProperties.class)
@ConditionalOnClass(Jedis.class)
/**
 * 加入开关控制，带有@EnableLocalRedis注解才会启用本地redis,search参数为搜索策略，默认为ALL，
 * CURRENT表示当前上下文，ANCESTORS搜索所有祖先，但不搜索当前上下文
 */
@ConditionalOnBean(annotation = EnableLocalRedis.class,search= SearchStrategy.CURRENT)
public class RedisAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(RedisAutoConfiguration.class);

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    @ConditionalOnMissingBean(Jedis.class)
    public Jedis jedis() {
        logger.info("==============自动配置redis:{}===============",redisProperties);
        return new Jedis(redisProperties.getMyhost(), redisProperties.getMyport());
    }
}
