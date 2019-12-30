package com.github.boot.configure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    /**
     * redis缓存管理器
     */
    @Bean
    @Primary
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
                // 默认缓存1小时
                .cacheDefaults(this.cacheConfiguration(Duration.ofHours(1)))
                .withInitialCacheConfigurations(this.cacheConfigurationMap())
                .build();
        return cacheManager;
    }

    /**
     * redis缓存管理器配置列表；
     * 可以根据业务需要配置不同的过期时间；
     */
    private Map<String, RedisCacheConfiguration> cacheConfigurationMap() {
        Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>();
        // 将菜单列缓存30分钟 cacheNames="MenuList"
        configurationMap.put("MenuList", this.cacheConfiguration(Duration.ofMinutes(30)));
        return configurationMap;
    }


    /**
     * redis缓存管理器的默认配置；
     * 使用Jackson序列化value,model不再需要实现Serializable接口；
     *
     * @param ttl 设置默认的过期时间，防止 redis 内存泄漏
     */
    private RedisCacheConfiguration cacheConfiguration(Duration ttl) {
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig();
        configuration = configuration
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .entryTtl(ttl);
        return configuration;
    }


    /**
     * 通用redis缓存key生成策略
     */
    @Override
    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }
}
