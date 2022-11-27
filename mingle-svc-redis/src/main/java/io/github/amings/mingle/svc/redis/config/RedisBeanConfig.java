package io.github.amings.mingle.svc.redis.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.amings.mingle.svc.redis.aspect.RedisLogAspect;
import io.github.amings.mingle.svc.redis.handler.RedisLogHandler;
import io.github.amings.mingle.svc.redis.handler.impl.RedisLogHandlerDefaultImpl;
import io.github.amings.mingle.utils.JacksonUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redis bean configuration
 *
 * @author Ming
 */

@Configuration
public class RedisBeanConfig {

    @Bean
    @ConditionalOnProperty(prefix = "mingle.svc.redis", name = "logging", havingValue = "enable")
    public RedisLogAspect redisLogAspect() {
        return new RedisLogAspect();
    }

    @Bean
    @ConditionalOnMissingBean(RedisLogHandler.class)
    public RedisLogHandler redisLogHandler() {
        return new RedisLogHandlerDefaultImpl();
    }

    @Bean("redisLogJacksonUtils")
    @ConditionalOnMissingBean(name = "redisLogJacksonUtils")
    public JacksonUtils redisLogJacksonUtils() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return new JacksonUtils(objectMapper);
    }

}
