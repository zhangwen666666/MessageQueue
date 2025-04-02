package com.zw.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class MessageService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage() {
        // 创建消息
        Message message = MessageBuilder.withBody("你好世界".getBytes()).build();
        // 往交换机exchange.direct上发消息，路由key是hello
        // 参数1：交换机名称，
        // 参数2：路由key，
        // 参数3：消息
        // 这里消息的路由key是hello与消息队列的都不匹配，因此消息会被交换机直接丢弃
//        rabbitTemplate.convertAndSend("exchange.direct", "hello", message);
        rabbitTemplate.convertAndSend("exchange.direct", "info", message);
        log.info("发送消息完毕，发送时间：{}", new Date());
    }
}
