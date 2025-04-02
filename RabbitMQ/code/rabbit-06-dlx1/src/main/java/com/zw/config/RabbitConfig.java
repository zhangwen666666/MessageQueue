package com.zw.config;

import lombok.Data;
import org.springframework.amqp.core.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties("my")
@Data
public class RabbitConfig {
    private String exchangeNormalName; // 正常交换机名称
    private String queueNormalName; // 正常队列名称
    private String exchangeDlxName; // 死信交换机名称
    private String queueDlxName; // 死信队列名称

    // 创建正常交换机
    @Bean
    public DirectExchange normalExchange() {
        return new DirectExchange(exchangeNormalName);
    }
    // 创建正常队列
    @Bean
    public Queue normalQueue(){
        // 设置队列的属性
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl", 35000); // 设置队列的过期时间
        arguments.put("x-dead-letter-exchange", exchangeDlxName); // 设置队列的死信交换机
        arguments.put("x-dead-letter-routing-key", "error"); // 设置队列的死信路由key
        return new Queue(queueNormalName, true, false, false, arguments);
    }
    // 绑定正常交换机和正常队列
    @Bean
    public Binding bindingNormal(DirectExchange normalExchange, Queue normalQueue) {
        return BindingBuilder.bind(normalQueue).to(normalExchange).with("order");
    }

    // 创建死信交换机
    @Bean
    public DirectExchange dlxExchange() {
        return ExchangeBuilder.directExchange(exchangeDlxName).build();
    }
    // 创建死信队列
    @Bean
    public Queue dlxQueue(){
        return QueueBuilder.durable(queueDlxName).build();
    }
    // 绑定死信交换机和死信队列
    @Bean
    public Binding bindingDlx(DirectExchange dlxExchange, Queue dlxQueue){
        return BindingBuilder.bind(dlxQueue).to(dlxExchange).with("error");
    }
}
