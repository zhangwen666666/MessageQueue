package com.zw.config;

import lombok.Data;
import org.springframework.amqp.core.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "my")
public class RabbitConfig {
    private String exchangeName;
    private String queueOrderName;
    private String queuePayName;
    private String queueDlxName;

    // 创建交换机
    @Bean
    public DirectExchange directExchange(){
        return ExchangeBuilder.directExchange(exchangeName).build();
    }

    // 创建订单队列
    @Bean
    public Queue queueOrder(){
        return QueueBuilder.durable(queueOrderName)
                .ttl(25000) // 设置过期时间
                .deadLetterExchange(exchangeName) // 绑定死信交换机(自身)
                .deadLetterRoutingKey("error") // 绑定死信路由key
                .build();
    }

    // 创建支付队列
    @Bean
    public Queue queuePay(){
        return QueueBuilder.durable(queuePayName)
                .ttl(15000) // 设置过期时间
                .deadLetterExchange(exchangeName) // 绑定死信交换机(自身)
                .deadLetterRoutingKey("error") // 绑定死信路由key
                .build();
    }

    // 创建死信消息队列
    @Bean
    public Queue queueDlx(){
        return QueueBuilder.durable(queueDlxName).build();
    }

    // 绑定订单队列到交换机
    @Bean
    public Binding bindingOrder(DirectExchange directExchange, Queue queueOrder){
        return BindingBuilder.bind(queueOrder).to(directExchange).with("order");
    }

    // 绑定订单队列到交换机
    @Bean
    public Binding bindingPay(DirectExchange directExchange, Queue queuePay){
        return BindingBuilder.bind(queuePay).to(directExchange).with("pay");
    }

    // 绑定死信队列到死信交换机
    @Bean
    public Binding bindingDlx(DirectExchange directExchange, Queue queueDlx){
        return BindingBuilder.bind(queueDlx).to(directExchange).with("error");
    }
}
