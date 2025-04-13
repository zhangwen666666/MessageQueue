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
    private String exchangeNormalName;
    private String exchangeAlterNateName;
    private String queueNormalName;
    private String queueAlterNateName;

    // 创建正常交换机
    @Bean
    public DirectExchange normalExchange(){
        return ExchangeBuilder.directExchange(exchangeNormalName)
                .alternate(exchangeAlterNateName) // 设置备用交换机
                .build();
    }

    // 创建正常消息队列
    @Bean
    public Queue normalQueue(){
        return QueueBuilder.durable(queueNormalName).build();
    }

    // 绑定正常队列到交换机
    @Bean
    public Binding binding(DirectExchange normalExchange, Queue normalQueue){
        return BindingBuilder.bind(normalQueue).to(normalExchange).with("info");
    }

    // 创建备用交换机
    @Bean
    public FanoutExchange alterExchange(){
        return ExchangeBuilder.fanoutExchange(exchangeAlterNateName).build();
    }

    // 创建备用队列
    @Bean
    public Queue alterQueue(){
        return QueueBuilder.durable(queueAlterNateName).build();
    }

    // 绑定备用队列到备用交换机
    @Bean
    public Binding bindingAlter(FanoutExchange alterExchange, Queue alterQueue){
        return BindingBuilder.bind(alterQueue).to(alterExchange);
    }
}
