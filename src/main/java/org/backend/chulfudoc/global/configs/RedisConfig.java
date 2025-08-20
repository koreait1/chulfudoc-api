package org.backend.chulfudoc.global.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisTemplate<String, Integer> redisTemplateInteger(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Integer> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 직렬화, 역직렬화를 위한 세팅
        template.setKeySerializer(new StringRedisSerializer());
        // 저장 시 String 불러올 시 Integer
        template.setValueSerializer(new GenericToStringSerializer<>(Integer.class));

        // Bean을 생성하면서 Serializer나 다른 프로퍼티를 설정한 직후, 내부적으로 필요한 초기화 작업을 실행
        template.afterPropertiesSet();

        return template;
    }
}
