package io.github.minguanq.mingle.svc.redis.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.minguanq.mingle.svc.handler.SerialNumberGeneratorHandler;
import io.github.minguanq.mingle.svc.redis.aspect.RedisLogAspect;
import io.github.minguanq.mingle.svc.redis.configuration.properties.SvcRedisProperties;
import io.github.minguanq.mingle.svc.redis.converter.ByteToRedisKeyConverter;
import io.github.minguanq.mingle.svc.redis.converter.RedisKeyToByteConverter;
import io.github.minguanq.mingle.svc.redis.converter.RedisKeyToStringConverter;
import io.github.minguanq.mingle.svc.redis.converter.StringToRedisKeyConverter;
import io.github.minguanq.mingle.svc.redis.handler.RedisLogHandler;
import io.github.minguanq.mingle.svc.redis.handler.impl.RedisLogHandlerDefaultImpl;
import io.github.minguanq.mingle.svc.utils.JacksonUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.util.Arrays;

/**
 * Configuration for redis bean
 *
 * @author Ming
 */
@Configuration
@EnableRedisRepositories
public class SvcRedisConfiguration {

    @Bean
    @ConfigurationProperties("mingle.svc.redis")
    public SvcRedisProperties redisProperties() {
        return new SvcRedisProperties();
    }

    @Bean
    public RedisCustomConversions redisCustomConversions() {
        return new RedisCustomConversions(Arrays.asList(stringToRedisKeyConverter(), byteToRedisKeyConverter(), redisKeyToByteConverter(), redisKeyToStringConverter()));
    }

    @Bean
    public ByteToRedisKeyConverter byteToRedisKeyConverter() {
        return new ByteToRedisKeyConverter();
    }

    @Bean
    public StringToRedisKeyConverter stringToRedisKeyConverter() {
        return new StringToRedisKeyConverter();
    }

    @Bean
    public RedisKeyToStringConverter redisKeyToStringConverter() {
        return new RedisKeyToStringConverter();
    }

    @Bean
    public RedisKeyToByteConverter redisKeyToByteConverter() {
        return new RedisKeyToByteConverter();
    }

    @Bean
    @ConditionalOnProperty(prefix = "mingle.svc.redis", name = "logging", havingValue = "true")
    public RedisLogAspect redisLogAspect(RedisLogHandler redisLogHandler, SerialNumberGeneratorHandler serialNumberGeneratorHandler) {
        return new RedisLogAspect(redisLogHandler, serialNumberGeneratorHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisLogHandler redisLogHandler() {
        return new RedisLogHandlerDefaultImpl(redisLogJacksonUtils());
    }

    @Bean("redisLogJacksonUtils")
    @ConditionalOnMissingBean(name = "redisLogJacksonUtils")
    public JacksonUtils redisLogJacksonUtils() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.findAndRegisterModules();
        return new JacksonUtils(objectMapper);
    }

}
