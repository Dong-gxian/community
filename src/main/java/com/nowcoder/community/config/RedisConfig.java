package com.nowcoder.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration

public class RedisConfig {
    //第三方的类就用@Bean注解来将其加入到容器中
    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory factory){//将连接工厂注入，由连接工厂来创建连接
        RedisTemplate<String, Object> template  =new RedisTemplate<>();
        template.setConnectionFactory(factory);

        //要设置序列化方式，把java类型的数据序列化存放到数据库中
        //设置key的序列化方式
        template.setKeySerializer(RedisSerializer.string());
        //设置value的序列化方式
        template.setValueSerializer(RedisSerializer.json());
        //设置hash的key的序列化方式
        template.setHashKeySerializer(RedisSerializer.string());
        //设置hash的value的序列化方式
        template.setHashValueSerializer(RedisSerializer.json());
        //触发设置生效
        template.afterPropertiesSet();
        return template;
    }
}
