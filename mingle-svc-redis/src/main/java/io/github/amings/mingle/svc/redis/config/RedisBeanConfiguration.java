package io.github.amings.mingle.svc.redis.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import io.github.amings.mingle.svc.handler.SerialNumberGeneratorHandler;
import io.github.amings.mingle.svc.redis.RedisEntity;
import io.github.amings.mingle.svc.redis.RedisKeyTemplate;
import io.github.amings.mingle.svc.redis.aspect.RedisLogAspect;
import io.github.amings.mingle.svc.redis.handler.RedisLogHandler;
import io.github.amings.mingle.svc.redis.handler.impl.RedisLogHandlerDefaultImpl;
import io.github.amings.mingle.svc.redis.serializer.RedisKeySerializer;
import io.github.amings.mingle.svc.utils.JacksonUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

/**
 * Configuration for redis bean
 *
 * @author Ming
 */
@Configuration
public class RedisBeanConfiguration {

    @Bean
    public <Entity extends RedisEntity> RedisKeyTemplate<Entity> redisKeyTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisKeyTemplate<Entity> redisTemplate = new RedisKeyTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        redisTemplate.setKeySerializer(new RedisKeySerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper()));
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    private ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        GenericJackson2JsonRedisSerializer.registerNullValueSerializer(objectMapper, null);
        StdTypeResolverBuilder typer = new ObjectMapper.DefaultTypeResolverBuilder(ObjectMapper.DefaultTyping.EVERYTHING, objectMapper.getPolymorphicTypeValidator())
                .init(JsonTypeInfo.Id.CLASS, null)
                .inclusion(JsonTypeInfo.As.PROPERTY);
        return objectMapper.setDefaultTyping(typer);
    }

    @Bean
    @ConditionalOnProperty(prefix = "mingle.svc.redis", name = "logging", havingValue = "enable")
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
