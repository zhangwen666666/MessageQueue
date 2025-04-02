package com.zw.config;

import lombok.Data;
import org.springframework.amqp.core.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Data
@ConfigurationProperties(prefix = "my")
public class RabbitConfig {
    private String exchangeName;
    private String queueAName;
    private String queueBName;

    // 创建交换机
    @Bean
    public HeadersExchange headersExchange(){
        return ExchangeBuilder.headersExchange(exchangeName).build();
    }

    // 创建消息队列
    @Bean
    public Queue queueA(){
        return QueueBuilder.durable(queueAName).build();
    }
    @Bean
    public Queue queueB(){
        return QueueBuilder.durable(queueBName).build();
    }

    // 绑定队列到交换机
    @Bean
    public Binding bindingA(HeadersExchange headersExchange, Queue queueA){
        Map<String, Object> headersValue = new HashMap<>();
        headersValue.put("type", "m");
        headersValue.put("status", "1");
        return BindingBuilder.bind(queueA).to(headersExchange).whereAll(headersValue).match();
    }
    @Bean
    public Binding bindingB1(HeadersExchange headersExchange, Queue queueB){
        Map<String, Object> headersValue = new HashMap<>();
        headersValue.put("type", "s");
        headersValue.put("status", "0");
        return BindingBuilder.bind(queueB).to(headersExchange).whereAll(headersValue).match();
    }
}
