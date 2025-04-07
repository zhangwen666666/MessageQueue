package com.zw.config;

import lombok.Data;
import org.springframework.amqp.core.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
        // 也可以采用建造者模式来绑定死信交换机和死信路由key
        return QueueBuilder.durable(queueNormalName)
                // .maxLength(5) // 设置队列的最大长度
                .expires(20000)
                .deadLetterExchange(exchangeDlxName) // 绑定死信交换机
                .deadLetterRoutingKey("error") // 绑定死信路由key
                .build();
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
