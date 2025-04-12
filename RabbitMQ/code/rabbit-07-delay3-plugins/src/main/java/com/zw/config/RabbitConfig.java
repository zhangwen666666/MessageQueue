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
    private String queueDelayName;

    // 创建自定义交换机
    @Bean
    public CustomExchange customExchange(){
        // 注意这里交换机不是直连交换机了，而是x-delayed-message类型的交换机
        // 因此需要自定义交换机
        // CustomExchange(String name, String type, boolean durable, boolean autoDelete, Map<String, Object> arguments)
        // name: 交换机名称
        // type: 交换机类型
        // durable: 是否持久化
        // autoDelete: 是否自动删除,ture表示自动删除，当所有与该交换机绑定的队列都断开连接后，该交换机自动删除
        // arguments: 其他参数，这里使用x-delayed-message类型的交换机的参数
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "direct");
        return new CustomExchange(exchangeName, "x-delayed-message", true, false,arguments);
    }

    // 创建消息队列
    @Bean
    public Queue queue(){
        // 不需要设置死信路由key了
        return QueueBuilder.durable(queueDelayName)
                .deadLetterExchange(exchangeName) // 绑定死信交换机(自身)
                .build();
    }

    // 绑定队列到交换机
    @Bean
    public Binding bindingNormal(CustomExchange customExchange, Queue queue){
        // 这里需要指定路由key
        return BindingBuilder.bind(queue).to(customExchange).with("plugin").noargs();
    }
}
