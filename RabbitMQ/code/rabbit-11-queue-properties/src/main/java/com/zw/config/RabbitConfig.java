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
    private String queueName;

    // 创建交换机
    @Bean
    public DirectExchange directExchange(){
        return ExchangeBuilder.directExchange(exchangeName).build();
    }

    // 创建消息队列
    @Bean
    public Queue queue(){
        // new Queue方式
        Map<String, Object> arguments = new HashMap<>();
        // return new Queue(queueName, true, false, true, arguments);

        // 构造者模式
         return QueueBuilder.durable(queueName)
                 .autoDelete() // 队列自动删除
                 .build();
    }

    // 绑定队列到交换机
    @Bean
    public Binding binding(DirectExchange directExchange, Queue queue){
        return BindingBuilder.bind(queue).to(directExchange).with("info");
    }
}
