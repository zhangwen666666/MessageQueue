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
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl", 30000); // 设置队列的过期时间

        // 方式一：使用 new Queue 的方式
        // Queue(String name, boolean durable, boolean exclusive, boolean autoDelete, Map arguments)
        // name: 队列名称
        // durable: 是否持久化
        // exclusive: 是否是排他队列
        // autoDelete: 是否自动删除
        // arguments: 其他参数
        // return new Queue(queueName, true, false, false, null);

        // 方式二：建造者模式
        return QueueBuilder.durable(queueName)
                .withArguments(arguments)
                .build();
    }

    // 绑定队列到交换机
    @Bean
    public Binding binding(DirectExchange directExchange, Queue queue){
        return BindingBuilder.bind(queue).to(directExchange).with("info");
    }
}
