package io.github.minguanqiu.mingle.svc.redis.configuration;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.databind.module.SimpleModule;
import io.github.minguanqiu.mingle.svc.handler.SerialNumberGeneratorHandler;
import io.github.minguanqiu.mingle.svc.redis.RedisEntity;
import io.github.minguanqiu.mingle.svc.redis.RedisKey;
import io.github.minguanqiu.mingle.svc.redis.RedisKeyTemplate;
import io.github.minguanqiu.mingle.svc.redis.aspect.RedisLogAspect;
import io.github.minguanqiu.mingle.svc.redis.configuration.properties.SvcRedisProperties;
import io.github.minguanqiu.mingle.svc.redis.converter.ByteToRedisKeyConverter;
import io.github.minguanqiu.mingle.svc.redis.converter.RedisKeyToByteConverter;
import io.github.minguanqiu.mingle.svc.redis.converter.RedisKeyToStringConverter;
import io.github.minguanqiu.mingle.svc.redis.converter.StringToRedisKeyConverter;
import io.github.minguanqiu.mingle.svc.redis.handler.RedisLogHandler;
import io.github.minguanqiu.mingle.svc.redis.handler.impl.RedisLogHandlerDefaultImpl;
import io.github.minguanqiu.mingle.svc.redis.serializer.RedisKeyJsonDeserializer;
import io.github.minguanqiu.mingle.svc.redis.serializer.RedisKeyJsonSerializer;
import io.github.minguanqiu.mingle.svc.redis.serializer.RedisKeySerializer;
import io.github.minguanqiu.mingle.svc.utils.JacksonUtils;
import java.util.Arrays;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

/**
 * Configuration for redis bean.
 *
 * @author Qiu Guan Ming
 */
@Configuration
public class SvcRedisConfiguration {

  @Bean
  @ConfigurationProperties("mingle.svc.redis")
  public SvcRedisProperties redisProperties() {
    return new SvcRedisProperties();
  }

  @Bean
  public <E extends RedisEntity> RedisKeyTemplate<E> redisKeyTemplate(
      LettuceConnectionFactory lettuceConnectionFactory) {
    RedisKeyTemplate<E> redisTemplate = new RedisKeyTemplate<>();
    redisTemplate.setConnectionFactory(lettuceConnectionFactory);
    redisTemplate.setKeySerializer(new RedisKeySerializer());
    redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper()));
    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }

  private ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    SimpleModule simpleModule = new SimpleModule();
    simpleModule.addSerializer(RedisKey.class, new RedisKeyJsonSerializer());
    simpleModule.addDeserializer(RedisKey.class, new RedisKeyJsonDeserializer());
    objectMapper.registerModule(simpleModule);
    objectMapper.findAndRegisterModules();
    GenericJackson2JsonRedisSerializer.registerNullValueSerializer(objectMapper, null);
    StdTypeResolverBuilder typer = new ObjectMapper.DefaultTypeResolverBuilder(
        DefaultTyping.NON_FINAL, objectMapper.getPolymorphicTypeValidator())
        .init(JsonTypeInfo.Id.CLASS, null)
        .inclusion(JsonTypeInfo.As.PROPERTY);
    return objectMapper.setDefaultTyping(typer);
  }

  @Bean
  @ConditionalOnMissingBean
  public RedisCustomConversions redisCustomConversions() {
    return new RedisCustomConversions(
        Arrays.asList(new RedisKeyToByteConverter(), new ByteToRedisKeyConverter(),
            new RedisKeyToStringConverter(), new StringToRedisKeyConverter()));
  }

  @Bean
  @ConditionalOnProperty(prefix = "mingle.svc.redis", name = "logging", havingValue = "true")
  public RedisLogAspect redisLogAspect(RedisLogHandler redisLogHandler,
      SerialNumberGeneratorHandler serialNumberGeneratorHandler) {
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

