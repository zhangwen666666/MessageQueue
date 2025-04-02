package com.zw.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    // 1.定义交换机
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("exchange.fanout");
    }

    // 2.定义队列
    @Bean
    public Queue queueA(){
        // 注意这里的Queue是org.springframework.amqp.core.Queue
        return new Queue("queue.fanout.a");
    }
    @Bean
    public Queue queueB(){
        // 注意这里的Queue是org.springframework.amqp.core.Queue
        return new Queue("queue.fanout.b");
    }

    // 3.绑定交换机和队列
    @Bean
    public Binding bindingA(FanoutExchange fanoutExchange, Queue queueA){
        // 绑定队列A到交换机
        return BindingBuilder.bind(queueA).to(fanoutExchange);
    }
    @Bean
    public Binding bindingB(FanoutExchange fanoutExchange, Queue queueB){
        // 绑定队列A到交换机
        return BindingBuilder.bind(queueB).to(fanoutExchange);
    }
}
