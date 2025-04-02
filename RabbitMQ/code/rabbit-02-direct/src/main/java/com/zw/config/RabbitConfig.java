package com.zw.config;

import lombok.Data;
import org.springframework.amqp.core.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "my")
@Data
public class RabbitConfig {
    private String exchangeName;
    private String queueAName;
    private String queueBName;

    // 创建交换机
    @Bean
    public DirectExchange directExchange() {
        return ExchangeBuilder.directExchange(exchangeName).build();
    }

    // 创建消息队列
    @Bean
    public Queue queueA() {
        return QueueBuilder.durable(queueAName).build();
    }
    @Bean
    public Queue queueB(){
        return QueueBuilder.durable(queueBName).build();
    }

    // 绑定交换机和队列
    @Bean
    public Binding bindingA(DirectExchange directExchange, Queue queueA) {
        // 将queueA绑定到directExchange交换机，并指定路由键为error
        return BindingBuilder.bind(queueA).to(directExchange).with("error");
    }
    @Bean
    public Binding bindingB1(DirectExchange directExchange, Queue queueB) {
        return BindingBuilder.bind(queueB).to(directExchange).with("error");
    }

    @Bean
    public Binding bindingB2(DirectExchange directExchange, Queue queueB) {
        return BindingBuilder.bind(queueB).to(directExchange).with("info");
    }

    @Bean
    public Binding bindingB3(DirectExchange directExchange, Queue queueB) {
        return BindingBuilder.bind(queueB).to(directExchange).with("warning");
    }
}
