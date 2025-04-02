package com.zw.config;

import lombok.Data;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class RabbitConfig {
    private String exchangeName;
    private String queueAName;
    private String queueBName;

    // 创建交换机
    @Bean
    public TopicExchange topicExchange(){
        return ExchangeBuilder.topicExchange(exchangeName).build();
    }

    // 创建消息队列
    @Bean
    public Queue queueA(){
        // durable:开启持久化
        return QueueBuilder.durable(queueAName).build();
    }
    @Bean
    public Queue queueB(){
        return QueueBuilder.durable(queueBName).build();
    }

    // 绑定队列到交换机
    @Bean
    public Binding bindingA(TopicExchange topicExchange, Queue queueA){
        return BindingBuilder.bind(queueA).to(topicExchange).with("*.orange.*");
    }
    @Bean
    public Binding bindingB1(TopicExchange topicExchange, Queue queueB){
        return BindingBuilder.bind(queueB).to(topicExchange).with("*.*.rabbit");
    }
    @Bean
    public Binding bindingB2(TopicExchange topicExchange, Queue queueB){
        return BindingBuilder.bind(queueB).to(topicExchange).with("laze.#");
    }
}
