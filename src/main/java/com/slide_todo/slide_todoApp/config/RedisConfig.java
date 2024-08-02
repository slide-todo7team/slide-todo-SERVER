package com.slide_todo.slide_todoApp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
public class RedisConfig {

  @Value("${spring.data.redis.host}")
  private String host;

  @Value("${spring.data.redis.port}")
  private int port;

  @Bean
  public LettuceConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
    redisConfig.setHostName(host);
    redisConfig.setPort(port);
    return new LettuceConnectionFactory(redisConfig);
  }

  /*블랙리스트 Redis 템플릿*/
  @Bean
  @Primary
  public RedisTemplate<String, String> blacklistRedisTemplate(
      LettuceConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory);
    redisTemplate.setKeySerializer(new Jackson2JsonRedisSerializer<>(String.class));
    redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
    return redisTemplate;
  }
}
